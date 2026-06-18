package edu.proyecto.expressfoodapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.proyecto.expressfoodapp.data.local.database.AppDatabaseProvider
import edu.proyecto.expressfoodapp.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabaseProvider.getDatabase(application)
    private val repository = ProductRepository(database.productDao())

    private val searchQuery = MutableStateFlow("")

    val products = searchQuery.flatMapLatest { query ->
        if (query.isBlank()) {
            repository.getLocalProducts()
        } else {
            repository.searchLocalProducts(query)
        }
    }

    fun syncProducts() {
        viewModelScope.launch {
            repository.syncProductsFromFirestore()
        }
    }

    fun searchProducts(query: String) {
        searchQuery.value = query
    }
}