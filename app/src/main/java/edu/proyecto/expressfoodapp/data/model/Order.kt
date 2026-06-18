package edu.proyecto.expressfoodapp.data.model

data class Order(
    val id: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val items: List<OrderItem> = emptyList(),
    val subtotal: Double = 0.0,
    val tax: Double = 0.0,
    val total: Double = 0.0,
    val status: String = "",
    val date: Long = 0
)