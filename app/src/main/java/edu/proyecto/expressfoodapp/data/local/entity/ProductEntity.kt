package edu.proyecto.expressfoodapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val ingredients: String,
    val rating: Double,
    val available: Boolean = true,
    val synced: Boolean = true
)