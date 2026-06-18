package edu.proyecto.expressfoodapp

import edu.proyecto.expressfoodapp.data.local.dao.CartDao
import edu.proyecto.expressfoodapp.data.local.entity.CartItemEntity
import edu.proyecto.expressfoodapp.data.repository.CartRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class CartRepositoryMockitoTest {

    private val cartDao = mock(CartDao::class.java)
    private val repository = CartRepository(cartDao)

    @Test
    fun addToCart_callsInsertItem() = runTest {
        val item = CartItemEntity(
            productId = "p1",
            productName = "Pizza",
            productPrice = 4900.0,
            imageUrl = "",
            quantity = 1
        )

        repository.addToCart(item)

        verify(cartDao).insertItem(item)
    }

    @Test
    fun updateItem_callsUpdateItem() = runTest {
        val item = CartItemEntity(
            productId = "p1",
            productName = "Pizza",
            productPrice = 4900.0,
            imageUrl = "",
            quantity = 2
        )

        repository.updateItem(item)

        verify(cartDao).updateItem(item)
    }

    @Test
    fun clearCart_callsClearCart() = runTest {
        repository.clearCart()

        verify(cartDao).clearCart()
    }
}