package edu.proyecto.expressfoodapp

import edu.proyecto.expressfoodapp.data.local.entity.OrderEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class OrderEntityTest {

    @Test
    fun orderEntity_storesCorrectValues() {

        val order = OrderEntity(
            id = "order1",
            userId = "user1",
            userEmail = "cliente@test.com",
            itemsJson = "[]",
            subtotal = 10000.0,
            tax = 1300.0,
            total = 11300.0,
            status = "PENDIENTE",
            date = 1781210338179,
            synced = false
        )

        assertEquals("order1", order.id)
        assertEquals("user1", order.userId)
        assertEquals("cliente@test.com", order.userEmail)
        assertEquals(10000.0, order.subtotal, 0.0)
        assertEquals(1300.0, order.tax, 0.0)
        assertEquals(11300.0, order.total, 0.0)
        assertEquals("PENDIENTE", order.status)

        assertFalse(order.synced)
    }
}