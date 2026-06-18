package edu.proyecto.expressfoodapp

import edu.proyecto.expressfoodapp.data.local.entity.ProductEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductEntityTest {

    @Test
    fun productEntity_storesCorrectValues() {
        val product = ProductEntity(
            id = "p1",
            name = "Hamburguesa Clásica",
            description = "Hamburguesa con carne y queso",
            price = 3500.0,
            imageUrl = "hamburguesa.jpg",
            ingredients = "Carne, queso, lechuga, tomate",
            rating = 4.5,
            available = true,
            synced = true
        )

        assertEquals("p1", product.id)
        assertEquals("Hamburguesa Clásica", product.name)
        assertEquals(3500.0, product.price, 0.0)
        assertEquals("Carne, queso, lechuga, tomate", product.ingredients)
        assertEquals(4.5, product.rating, 0.0)
        assertTrue(product.available)
        assertTrue(product.synced)
    }
}