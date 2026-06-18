package edu.proyecto.expressfoodapp.ui.client

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import edu.proyecto.expressfoodapp.R
import edu.proyecto.expressfoodapp.data.local.entity.CartItemEntity
import edu.proyecto.expressfoodapp.data.local.entity.ProductEntity
import edu.proyecto.expressfoodapp.databinding.ActivityClientBinding
import edu.proyecto.expressfoodapp.databinding.DialogAddToCartBinding
import edu.proyecto.expressfoodapp.databinding.DialogProductDetailBinding
import edu.proyecto.expressfoodapp.ui.auth.LoginActivity
import edu.proyecto.expressfoodapp.ui.client.cart.CartActivity
import edu.proyecto.expressfoodapp.ui.client.menu.ProductAdapter
import edu.proyecto.expressfoodapp.ui.client.orders.OrdersActivity
import edu.proyecto.expressfoodapp.ui.client.reports.ClientReportActivity
import edu.proyecto.expressfoodapp.utils.NetworkUtils
import edu.proyecto.expressfoodapp.utils.SyncUtils
import edu.proyecto.expressfoodapp.viewmodel.CartViewModel
import edu.proyecto.expressfoodapp.viewmodel.CartViewModelFactory
import edu.proyecto.expressfoodapp.viewmodel.ProductViewModel
import edu.proyecto.expressfoodapp.viewmodel.ProductViewModelFactory
import kotlinx.coroutines.launch

class ClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var productAdapter: ProductAdapter

    private val productViewModel: ProductViewModel by viewModels {
        ProductViewModelFactory(application)
    }

    private val cartViewModel: CartViewModel by viewModels {
        CartViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupRecyclerView()
        setupSearchView()
        observeProducts()
        setupNavbar()
        updateConnectionStatus()
        setupLogout()

        productViewModel.syncProducts()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(
            onAddToCartClick = { product ->
                showAddToCartDialog(product)
            },
            onProductDetailClick = { product ->
                showProductDetailDialog(product)
            }
        )

        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(this@ClientActivity)
            adapter = productAdapter
        }
    }

    private fun showProductDetailDialog(product: ProductEntity) {
        val dialogBinding = DialogProductDetailBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogBinding.tvDetailProductName.text = product.name
        dialogBinding.tvDetailProductPrice.text = "₡${product.price}"
        dialogBinding.tvDetailProductRating.text = "⭐ ${product.rating}"
        dialogBinding.tvDetailIngredients.text = product.ingredients
        dialogBinding.tvDetailDescription.text = product.description

        Glide.with(this)
            .load(product.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.logo_express_food)
            .into(dialogBinding.imgDetailProduct)

        dialogBinding.btnCloseDetail.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showAddToCartDialog(product: ProductEntity) {
        val dialogBinding = DialogAddToCartBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        // Configurar fondo transparente para que se vea el redondeado del CardView
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Cargar datos del producto
        dialogBinding.tvDialogProductName.text = product.name
        dialogBinding.tvDialogProductPrice.text = "₡${product.price}"
        
        Glide.with(this)
            .load(product.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.logo_express_food)
            .into(dialogBinding.imgDialogProduct)

        var quantity = 1

        dialogBinding.btnPlus.setOnClickListener {
            quantity++
            dialogBinding.tvQuantity.text = quantity.toString()
        }

        dialogBinding.btnMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                dialogBinding.tvQuantity.text = quantity.toString()
            }
        }

        dialogBinding.btnConfirmOrder.setOnClickListener {
            val cartItem = CartItemEntity(
                productId = product.id,
                productName = product.name,
                productPrice = product.price,
                imageUrl = product.imageUrl,
                quantity = quantity
            )
            cartViewModel.addToCart(cartItem)
            Toast.makeText(this, "${product.name} (x$quantity) agregado al carrito", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setupNavbar() {
        // Indicador visual: Estamos en Menú
        binding.btnOpenMenu.setBackgroundColor(getColor(R.color.express_primary_light))
        binding.btnOpenMenu.setTextColor(getColor(R.color.express_primary_dark))
        
        binding.btnOpenOrders.setOnClickListener {
            startActivity(Intent(this, OrdersActivity::class.java))
        }
        binding.btnOpenCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        binding.btnClientReports.setOnClickListener {
            startActivity(Intent(this, ClientReportActivity::class.java))
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(
            object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    productViewModel.searchProducts(query.orEmpty())
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    productViewModel.searchProducts(newText.orEmpty())
                    return true
                }
            }
        )
    }

    private fun observeProducts() {
        lifecycleScope.launch {
            productViewModel.products.collect { products ->
                productAdapter.submitList(products)
            }
        }
    }

    private fun setupLogout() {
        binding.header.btnLogout.visibility = View.VISIBLE
        binding.header.btnLogout.setOnClickListener {
            auth.signOut()
            val googleClient = GoogleSignIn.getClient(this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build())
            googleClient.signOut().addOnCompleteListener {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun updateConnectionStatus() {
        if (NetworkUtils.isOnline(this)) {
            binding.tvConnectionStatus.text = "Online"
            binding.tvConnectionStatus.setTextColor(Color.GREEN)
            SyncUtils.syncPendingOrders(this)
            productViewModel.syncProducts()
        } else {
            binding.tvConnectionStatus.text = "Offline"
            binding.tvConnectionStatus.setTextColor(Color.RED)
        }
    }
}