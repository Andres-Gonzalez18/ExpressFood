package edu.proyecto.expressfoodapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(

    @PrimaryKey
    val productId: String,

    val productName: String,

    val productPrice: Double,

    val imageUrl: String,

    val quantity: Int
)