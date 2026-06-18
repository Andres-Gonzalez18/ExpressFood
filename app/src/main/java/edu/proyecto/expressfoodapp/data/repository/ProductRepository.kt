package edu.proyecto.expressfoodapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import edu.proyecto.expressfoodapp.data.local.dao.ProductDao
import edu.proyecto.expressfoodapp.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class ProductRepository(
    private val productDao: ProductDao
) {

    private val firestore = FirebaseFirestore.getInstance()

    fun getLocalProducts(): Flow<List<ProductEntity>> {
        return productDao.getAllProducts()
    }

    fun searchLocalProducts(query: String): Flow<List<ProductEntity>> {
        return productDao.searchProducts(query)
    }

    suspend fun syncProductsFromFirestore() {
        val snapshot = firestore.collection("products")
            .whereEqualTo("available", true)
            .get()
            .await()

        val products = snapshot.documents.map { document ->
            ProductEntity(
                id = document.id,
                name = document.getString("name") ?: "",
                description = document.getString("description") ?: "",
                price = document.getDouble("price") ?: 0.0,
                imageUrl = document.getString("imageUrl") ?: "",
                ingredients = document.getString("ingredients") ?: "",
                rating = document.getDouble("rating") ?: 0.0,
                available = document.getBoolean("available") ?: true,
                synced = true
            )
        }

        productDao.deleteAll()
        productDao.insertProducts(products)
    }
}