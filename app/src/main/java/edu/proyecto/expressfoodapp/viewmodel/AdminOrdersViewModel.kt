package edu.proyecto.expressfoodapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.proyecto.expressfoodapp.data.model.Order
import edu.proyecto.expressfoodapp.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminOrdersViewModel : ViewModel() {

    private val repository = OrderRepository()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    fun loadOrders() {
        viewModelScope.launch {
            _orders.value = repository.getAllOrders()
        }
    }

    fun updateStatus(
        orderId: String,
        status: String
    ) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, status)
            loadOrders()
        }
    }
}