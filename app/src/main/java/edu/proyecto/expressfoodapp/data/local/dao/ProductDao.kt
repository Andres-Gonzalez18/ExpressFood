package edu.proyecto.expressfoodapp.data.local.dao

import androidx.room.*
import edu.proyecto.expressfoodapp.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products WHERE available = 1")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("""
        SELECT * FROM products 
        WHERE available = 1 
        AND (name LIKE '%' || :query || '%' 
        OR ingredients LIKE '%' || :query || '%')
    """)
    fun searchProducts(query: String): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Query("DELETE FROM products")
    suspend fun deleteAll()
}