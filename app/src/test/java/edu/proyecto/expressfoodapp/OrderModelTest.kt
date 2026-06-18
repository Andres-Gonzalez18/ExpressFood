package edu.proyecto.expressfoodapp

import edu.proyecto.expressfoodapp.data.model.Order
import edu.proyecto.expressfoodapp.data.model.OrderItem
import org.junit.Assert.assertEquals
import org.junit.Test

class OrderModelTest {

    @Test
    fun orderItem_storesCorrectValues() {
        val item = OrderItem(
            productId = "p1",
            productName = "Pizza",
            productPrice = 4900.0,
            imageUrl = "pizza.jpg",
            quantity = 2
        )

        assertEquals("p1", item.productId)
        assertEquals("Pizza", item.productName)
        assertEquals(4900.0, item.productPrice, 0.0)
        assertEquals("pizza.jpg", item.imageUrl)
        assertEquals(2, item.quantity)
    }

    @Test
    fun order_storesCorrectValues() {
        val item = OrderItem(
            productId = "p1",
            productName = "Pizza",
            productPrice = 4900.0,
            imageUrl = "pizza.jpg",
            quantity = 1
        )

        val order = Order(
            id = "order1",
            userId = "user1",
            userEmail = "cliente@test.com",
            items = listOf(item),
            subtotal = 4900.0,
            tax = 637.0,
            total = 5537.0,
            status = "PENDIENTE",
            date = 1781210338179
        )

        assertEquals("order1", order.id)
        assertEquals("user1", order.userId)
        assertEquals("cliente@test.com", order.userEmail)
        assertEquals(1, order.items.size)
        assertEquals(4900.0, order.subtotal, 0.0)
        assertEquals(637.0, order.tax, 0.0)
        assertEquals(5537.0, order.total, 0.0)
        assertEquals("PENDIENTE", order.status)
        assertEquals(1781210338179, order.date)
    }
}