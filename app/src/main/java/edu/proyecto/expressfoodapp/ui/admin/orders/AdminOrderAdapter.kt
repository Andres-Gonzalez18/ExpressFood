package edu.proyecto.expressfoodapp.ui.admin.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.proyecto.expressfoodapp.data.model.Order
import edu.proyecto.expressfoodapp.databinding.ItemAdminOrderBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdminOrderAdapter(
    private val onStatusChanged: (Order, String) -> Unit
) : RecyclerView.Adapter<AdminOrderAdapter.AdminOrderViewHolder>() {

    private var orders = emptyList<Order>()

    private val statusOptions = listOf(
        "PENDIENTE",
        "EN CAMINO",
        "ENTREGADA",
        "CANCELADA"
    )

    fun submitList(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }

    inner class AdminOrderViewHolder(
        private val binding: ItemAdminOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            binding.tvAdminOrderId.text = "Orden: ${order.id.take(8)}"
            binding.tvAdminClient.text = "Cliente: ${order.userEmail}"
            binding.tvAdminDate.text = "Fecha: ${formatDate(order.date)}"
            binding.tvAdminTotal.text = "Total: ₡${order.total}"

            val productsText = order.items.joinToString(separator = "\n") { item ->
                "${item.productName} x${item.quantity}"
            }
            binding.tvAdminProducts.text = "Productos:\n$productsText"

            val spinnerAdapter = ArrayAdapter(
                binding.root.context,
                android.R.layout.simple_spinner_item,
                statusOptions
            )
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerOrderStatus.adapter = spinnerAdapter

            val currentIndex = statusOptions.indexOf(order.status)
            if (currentIndex >= 0) {
                binding.spinnerOrderStatus.setSelection(currentIndex)
            }

            binding.spinnerOrderStatus.onItemSelectedListener =
                object : android.widget.AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: android.widget.AdapterView<*>?,
                        view: android.view.View?,
                        position: Int,
                        id: Long
                    ) {
                        val selectedStatus = statusOptions[position]
                        if (selectedStatus != order.status) {
                            onStatusChanged(order, selectedStatus)
                        }
                    }

                    override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
                }
        }

        private fun formatDate(timestamp: Long): String {
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            return formatter.format(Date(timestamp))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminOrderViewHolder {
        val binding = ItemAdminOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AdminOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminOrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size
}