package edu.proyecto.expressfoodapp.data.local.dao

import androidx.room.*
import edu.proyecto.expressfoodapp.data.local.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Query("SELECT * FROM orders ORDER BY date DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY date DESC")
    fun getOrdersByUser(userId: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE synced = 0")
    suspend fun getUnsyncedOrders(): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE status IN ('PENDIENTE', 'EN CAMINO')")
    suspend fun getActiveOrders(): List<OrderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Query("UPDATE orders SET status = 'CANCELADA', synced = 0 WHERE id = :orderId")
    suspend fun cancelOrderLocally(orderId: String)

    @Query("UPDATE orders SET synced = 1 WHERE id = :orderId")
    suspend fun markAsSynced(orderId: String)
}