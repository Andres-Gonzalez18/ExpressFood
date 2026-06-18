package edu.proyecto.expressfoodapp.data.repository

import edu.proyecto.expressfoodapp.data.local.dao.CartDao
import edu.proyecto.expressfoodapp.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

class CartRepository(
    private val cartDao: CartDao
) {

    fun getCartItems(): Flow<List<CartItemEntity>> {
        return cartDao.getCartItems()
    }

    suspend fun addToCart(item: CartItemEntity) {
        cartDao.insertItem(item)
    }

    suspend fun updateItem(item: CartItemEntity) {
        cartDao.updateItem(item)
    }

    suspend fun deleteItem(item: CartItemEntity) {
        cartDao.deleteItem(item)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }

    fun calculateSubtotal(items: List<CartItemEntity>): Double {
        return items.sumOf { it.productPrice * it.quantity }
    }

    fun calculateTax(subtotal: Double): Double {
        return subtotal * 0.13
    }

    fun calculateTotal(subtotal: Double, tax: Double): Double {
        return subtotal + tax
    }
}