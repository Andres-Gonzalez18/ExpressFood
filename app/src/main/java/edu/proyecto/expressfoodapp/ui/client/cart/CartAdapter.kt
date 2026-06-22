package edu.proyecto.expressfoodapp.ui.client.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.proyecto.expressfoodapp.data.local.entity.CartItemEntity
import edu.proyecto.expressfoodapp.databinding.ItemCartBinding

class CartAdapter(
    private val onIncrease: (CartItemEntity) -> Unit,
    private val onDecrease: (CartItemEntity) -> Unit,
    private val onRemove: (CartItemEntity) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var items = emptyList<CartItemEntity>()

    fun submitList(newItems: List<CartItemEntity>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class CartViewHolder(
        private val binding: ItemCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItemEntity) {
            binding.tvName.text = item.productName
            binding.tvPrice.text = "Precio: ₡${item.productPrice}"
            binding.tvQuantity.text = "Cantidad: ${item.quantity}"

            Glide.with(binding.imgCartProduct.context)
                .load(item.imageUrl)
                .centerCrop()
                .into(binding.imgCartProduct)

            binding.btnIncrease.setOnClickListener {
                onIncrease(item)
            }

            binding.btnDecrease.setOnClickListener {
                onDecrease(item)
            }

            binding.btnRemove.setOnClickListener {
                onRemove(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}