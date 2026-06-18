package edu.proyecto.expressfoodapp.ui.client.cart

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import edu.proyecto.expressfoodapp.R
import edu.proyecto.expressfoodapp.data.local.entity.CartItemEntity
import edu.proyecto.expressfoodapp.databinding.ActivityCartBinding
import edu.proyecto.expressfoodapp.ui.client.ClientActivity
import edu.proyecto.expressfoodapp.ui.client.orders.OrdersActivity
import edu.proyecto.expressfoodapp.ui.client.reports.ClientReportActivity
import edu.proyecto.expressfoodapp.viewmodel.CartViewModel
import edu.proyecto.expressfoodapp.viewmodel.CartViewModelFactory
import kotlinx.coroutines.launch
import edu.proyecto.expressfoodapp.utils.SyncUtils

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter
    private var currentItems: List<CartItemEntity> = emptyList()

    private val cartViewModel: CartViewModel by viewModels {
        CartViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeCart()
        setupCheckout()
        setupNavbar()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onIncrease = { item ->
                cartViewModel.updateItem(
                    item.copy(quantity = item.quantity + 1)
                )
            },
            onDecrease = { item ->
                if (item.quantity > 1) {
                    cartViewModel.updateItem(
                        item.copy(quantity = item.quantity - 1)
                    )
                } else {
                    cartViewModel.removeItem(item)
                }
            },
            onRemove = { item ->
                cartViewModel.removeItem(item)
            }
        )

        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
            adapter = cartAdapter
        }
    }

    private fun observeCart() {
        lifecycleScope.launch {
            cartViewModel.cartItems.collect { items ->
                currentItems = items
                cartAdapter.submitList(items)
                cartViewModel.calculateTotals(items)
            }
        }

        lifecycleScope.launch {
            cartViewModel.subtotal.collect { subtotal ->
                binding.tvSubtotal.text = "Subtotal: ₡$subtotal"
            }
        }

        lifecycleScope.launch {
            cartViewModel.tax.collect { tax ->
                binding.tvTax.text = "IVA 13%: ₡$tax"
            }
        }

        lifecycleScope.launch {
            cartViewModel.total.collect { total ->
                binding.tvTotal.text = "Total: ₡$total"
            }
        }
    }

    private fun setupNavbar() {
        // Indicador visual: Estamos en Carrito
        binding.btnOpenCart.setBackgroundColor(getColor(R.color.express_primary_light))
        binding.btnOpenCart.setTextColor(getColor(R.color.express_primary_dark))

        binding.btnOpenMenu.setOnClickListener {
            startActivity(Intent(this, ClientActivity::class.java))
            finish()
        }
        binding.btnOpenOrders.setOnClickListener {
            startActivity(Intent(this, OrdersActivity::class.java))
            finish()
        }
        binding.btnClientReports.setOnClickListener {
            startActivity(Intent(this, ClientReportActivity::class.java))
            finish()
        }
    }

    private fun setupCheckout() {
        binding.btnCheckout.setOnClickListener {

            binding.btnCheckout.isEnabled = false
            binding.btnCheckout.text = "Procesando..."

            cartViewModel.processOrder(
                items = currentItems,
                onSuccess = {
                    SyncUtils.syncPendingOrders(this)

                    Toast.makeText(
                        this,
                        "Orden guardada. Se sincronizará cuando haya internet.",
                        Toast.LENGTH_LONG
                    ).show()

                    finish()
                },
                onError = { message ->
                    binding.btnCheckout.isEnabled = true
                    binding.btnCheckout.text = "Procesar Orden"

                    Toast.makeText(
                        this,
                        message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }
}