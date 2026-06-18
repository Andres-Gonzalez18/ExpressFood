package edu.proyecto.expressfoodapp.data.model

data class DailyReport(
    val date: String,
    val orderCount: Int,
    val totalAmount: Double
)