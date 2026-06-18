package edu.proyecto.expressfoodapp

import edu.proyecto.expressfoodapp.data.local.entity.CartItemEntity
import edu.proyecto.expressfoodapp.data.repository.CartRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class CartRepositoryTest {

    private val repository = CartRepository(
        cartDao = FakeCartDao()
    )

    @Test
    fun calculateSubtotal_returnsCorrectValue() {
        val items = listOf(
            CartItemEntity(
                productId = "1",
                productName = "Pizza",
                productPrice = 5000.0,
                imageUrl = "",
                quantity = 2
            ),
            CartItemEntity(
                productId = "2",
                productName = "Hamburguesa",
                productPrice = 3000.0,
                imageUrl = "",
                quantity = 1
            )
        )

        val result = repository.calculateSubtotal(items)

        assertEquals(13000.0, result, 0.0)
    }

    @Test
    fun calculateTax_returnsThirteenPercent() {
        val subtotal = 10000.0

        val result = repository.calculateTax(subtotal)

        assertEquals(1300.0, result, 0.0)
    }

    @Test
    fun calculateTotal_returnsSubtotalPlusTax() {
        val subtotal = 10000.0
        val tax = 1300.0

        val result = repository.calculateTotal(subtotal, tax)

        assertEquals(11300.0, result, 0.0)
    }
}