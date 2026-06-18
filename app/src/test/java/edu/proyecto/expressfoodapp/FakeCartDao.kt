package edu.proyecto.expressfoodapp

import edu.proyecto.expressfoodapp.data.local.dao.CartDao
import edu.proyecto.expressfoodapp.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCartDao : CartDao {

    override fun getCartItems(): Flow<List<CartItemEntity>> {
        return flowOf(emptyList())
    }

    override suspend fun insertItem(item: CartItemEntity) {
    }

    override suspend fun updateItem(item: CartItemEntity) {
    }

    override suspend fun deleteItem(item: CartItemEntity) {
    }

    override suspend fun clearCart() {
    }
}