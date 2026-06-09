package edu.proyecto.expressfoodapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val id: String,

    val userId: String,
    val total: Double,
    val status: String,
    val date: Long,

    val synced: Boolean = false
)