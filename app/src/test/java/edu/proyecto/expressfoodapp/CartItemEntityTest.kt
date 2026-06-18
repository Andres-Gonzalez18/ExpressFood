package edu.proyecto.expressfoodapp

import edu.proyecto.expressfoodapp.data.local.entity.CartItemEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class CartItemEntityTest {

    @Test
    fun cartItem_storesCorrectValues() {

        val item = CartItemEntity(
            productId = "p1",
            productName = "Pizza Clásica",
            productPrice = 4900.0,
            imageUrl = "pizza.jpg",
            quantity = 3
        )

        assertEquals("p1", item.productId)
        assertEquals("Pizza Clásica", item.productName)
        assertEquals(4900.0, item.productPrice, 0.0)
        assertEquals("pizza.jpg", item.imageUrl)
        assertEquals(3, item.quantity)
    }
}