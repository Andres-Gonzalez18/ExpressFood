package edu.proyecto.expressfoodapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.proyecto.expressfoodapp.data.local.dao.CartDao
import edu.proyecto.expressfoodapp.data.local.dao.OrderDao
import edu.proyecto.expressfoodapp.data.local.dao.ProductDao
import edu.proyecto.expressfoodapp.data.local.dao.UserDao
import edu.proyecto.expressfoodapp.data.local.entity.CartItemEntity
import edu.proyecto.expressfoodapp.data.local.entity.OrderEntity
import edu.proyecto.expressfoodapp.data.local.entity.ProductEntity
import edu.proyecto.expressfoodapp.data.local.entity.UserEntity

@Database(
    entities = [
        ProductEntity::class,
        OrderEntity::class,
        UserEntity::class,
        CartItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    abstract fun orderDao(): OrderDao

    abstract fun userDao(): UserDao

    abstract fun cartDao(): CartDao
}