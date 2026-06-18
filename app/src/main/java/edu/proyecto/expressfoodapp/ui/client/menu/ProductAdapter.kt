package edu.proyecto.expressfoodapp.ui.client.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.proyecto.expressfoodapp.data.local.entity.ProductEntity
import edu.proyecto.expressfoodapp.databinding.ItemProductBinding

class ProductAdapter(
    private val onAddToCartClick: (ProductEntity) -> Unit,
    private val onProductDetailClick: (ProductEntity) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var products: List<ProductEntity> = emptyList()

    fun submitList(newProducts: List<ProductEntity>) {
        products = newProducts
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductEntity) {
            binding.tvProductName.text = product.name
            binding.tvIngredients.text = product.ingredients
            binding.tvPrice.text = "₡${product.price}"
            binding.tvRating.text = "⭐ ${product.rating}"

            Glide.with(binding.imgProduct.context)
                .load(product.imageUrl)
                .centerCrop()
                .into(binding.imgProduct)

            // Clic en el botón específico de agregar
            binding.btnAddToCart.setOnClickListener {
                onAddToCartClick(product)
            }
            
            // Clic en toda la tarjeta para ver el detalle
            binding.root.setOnClickListener {
                onProductDetailClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size
}