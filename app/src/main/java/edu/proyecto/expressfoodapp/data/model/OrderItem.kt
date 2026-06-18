package edu.proyecto.expressfoodapp.data.model

data class OrderItem(
    val productId: String = "",
    val productName: String = "",
    val productPrice: Double = 0.0,
    val imageUrl: String = "",
    val quantity: Int = 0
)