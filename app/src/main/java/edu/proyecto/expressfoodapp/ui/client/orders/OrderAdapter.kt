package edu.proyecto.expressfoodapp.ui.client.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import edu.proyecto.expressfoodapp.R
import edu.proyecto.expressfoodapp.data.model.Order
import edu.proyecto.expressfoodapp.databinding.ItemOrderBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private var orders = emptyList<Order>()

    fun submitList(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }

    inner class OrderViewHolder(
        private val binding: ItemOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            binding.tvOrderId.text = "Orden: ${order.id.take(8)}"
            binding.tvOrderDate.text = "Fecha: ${formatDate(order.date)}"
            binding.tvOrderStatus.text = order.status
            binding.tvOrderTotal.text = "Total: ₡${order.total}"

            val productsText = order.items.joinToString(separator = "\n") { item ->
                "${item.productName} x${item.quantity}"
            }

            binding.tvOrderProducts.text = "Productos:\n$productsText"

            // Aplicar colores según el estado
            val context = binding.root.context
            val colorRes = when (order.status) {
                "PENDIENTE" -> R.color.status_pending
                "EN CAMINO" -> R.color.status_shipping
                "ENTREGADA" -> R.color.status_delivered
                "CANCELADA" -> R.color.status_cancelled
                else -> R.color.express_primary
            }
            binding.tvOrderStatus.setTextColor(ContextCompat.getColor(context, colorRes))
        }

        private fun formatDate(timestamp: Long): String {
            val formatter = SimpleDateFormat(
                "dd/MM/yyyy HH:mm",
                Locale.getDefault()
            )
            return formatter.format(Date(timestamp))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size
}