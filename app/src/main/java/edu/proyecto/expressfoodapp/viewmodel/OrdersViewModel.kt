package edu.proyecto.expressfoodapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.proyecto.expressfoodapp.data.model.Order
import edu.proyecto.expressfoodapp.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrdersViewModel : ViewModel() {

    private val repository = OrderRepository()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _filteredOrders = MutableStateFlow<List<Order>>(emptyList())
    val filteredOrders: StateFlow<List<Order>> = _filteredOrders

    private var currentStatus: String = "TODAS"

    fun loadOrders() {
        viewModelScope.launch {
            val result = repository.getUserOrders()
            _orders.value = result
            applyFilter(currentStatus)
        }
    }

    fun applyFilter(status: String) {
        currentStatus = status

        _filteredOrders.value = if (status == "TODAS") {
            _orders.value
        } else {
            _orders.value.filter { it.status == status }
        }
    }
}