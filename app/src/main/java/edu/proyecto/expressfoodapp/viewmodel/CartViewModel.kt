package edu.proyecto.expressfoodapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.proyecto.expressfoodapp.data.local.database.AppDatabaseProvider
import edu.proyecto.expressfoodapp.data.local.entity.CartItemEntity
import edu.proyecto.expressfoodapp.data.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import edu.proyecto.expressfoodapp.data.repository.OrderRepository

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabaseProvider.getDatabase(application)
    private val repository = CartRepository(database.cartDao())
    private val orderRepository = OrderRepository(database.orderDao())

    val cartItems = repository.getCartItems()

    private val _subtotal = MutableStateFlow(0.0)
    val subtotal: StateFlow<Double> = _subtotal

    private val _tax = MutableStateFlow(0.0)
    val tax: StateFlow<Double> = _tax

    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total

    fun addToCart(item: CartItemEntity) {
        viewModelScope.launch {
            val currentItems = repository.getCartItems().first()
            val existingItem = currentItems.find { it.productId == item.productId }

            if (existingItem != null) {
                repository.updateItem(existingItem.copy(quantity = existingItem.quantity + 1))
            } else {
                repository.addToCart(item)
            }
        }
    }

    fun updateItem(item: CartItemEntity) {
        viewModelScope.launch {
            repository.updateItem(item)
        }
    }

    fun removeItem(item: CartItemEntity) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    fun calculateTotals(items: List<CartItemEntity>) {
        val subtotalValue = repository.calculateSubtotal(items)
        val taxValue = repository.calculateTax(subtotalValue)

        _subtotal.value = subtotalValue
        _tax.value = taxValue
        _total.value = repository.calculateTotal(subtotalValue, taxValue)
    }

    fun processOrder(
        items: List<CartItemEntity>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (items.isEmpty()) {
                    onError("El carrito está vacío")
                    return@launch
                }

                val subtotalValue = repository.calculateSubtotal(items)
                val taxValue = repository.calculateTax(subtotalValue)
                val totalValue = repository.calculateTotal(subtotalValue, taxValue)

                orderRepository.createOrder(
                    items = items,
                    subtotal = subtotalValue,
                    tax = taxValue,
                    total = totalValue
                )

                repository.clearCart()
                onSuccess()

            } catch (e: Exception) {
                onError(e.message ?: "Error al procesar la orden")
            }
        }
    }
}