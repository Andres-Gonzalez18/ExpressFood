package edu.proyecto.expressfoodapp.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.proyecto.expressfoodapp.data.local.dao.OrderDao
import edu.proyecto.expressfoodapp.data.local.entity.CartItemEntity
import edu.proyecto.expressfoodapp.data.local.entity.OrderEntity
import edu.proyecto.expressfoodapp.data.model.Order
import edu.proyecto.expressfoodapp.data.model.OrderItem
import kotlinx.coroutines.tasks.await
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class OrderRepository(
    private val orderDao: OrderDao? = null
) {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun createOrder(
        items: List<CartItemEntity>,
        subtotal: Double,
        tax: Double,
        total: Double
    ) {
        val user = auth.currentUser ?: throw Exception("Usuario no autenticado")

        val orderId = UUID.randomUUID().toString()
        val date = System.currentTimeMillis()
        val itemsJson = cartItemsToJson(items)

        val localOrder = OrderEntity(
            id = orderId,
            userId = user.uid,
            userEmail = user.email ?: "",
            itemsJson = itemsJson,
            subtotal = subtotal,
            tax = tax,
            total = total,
            status = "PENDIENTE",
            date = date,
            synced = false
        )

        orderDao?.insertOrder(localOrder)
    }

    suspend fun getUserOrders(): List<Order> {
        val user = auth.currentUser ?: return emptyList()

        return try {
            val snapshot = firestore.collection("orders")
                .whereEqualTo("userId", user.uid)
                .get()
                .await()
            mapSnapshotToOrders(snapshot.documents)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getAllOrders(): List<Order> {
        return try {
            val snapshot = firestore.collection("orders")
                .get()
                .await()
            mapSnapshotToOrders(snapshot.documents)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateOrderStatus(
        orderId: String,
        status: String
    ) {
        firestore.collection("orders")
            .document(orderId)
            .update("status", status)
            .await()
    }

    private fun cartItemsToJson(items: List<CartItemEntity>): String {
        val array = JSONArray()
        items.forEach { item ->
            val obj = JSONObject()
            obj.put("productId", item.productId)
            obj.put("productName", item.productName)
            obj.put("productPrice", item.productPrice)
            obj.put("imageUrl", item.imageUrl)
            obj.put("quantity", item.quantity)
            array.put(obj)
        }
        return array.toString()
    }

    private fun mapSnapshotToOrders(
        documents: List<com.google.firebase.firestore.DocumentSnapshot>
    ): List<Order> {
        return documents.mapNotNull { document ->
            try {
                val rawItems = document.get("items") as? List<Map<String, Any>> ?: emptyList()
                val orderItems = rawItems.map { item ->
                    OrderItem(
                        productId = item["productId"] as? String ?: "",
                        productName = item["productName"] as? String ?: "",
                        productPrice = when (val p = item["productPrice"]) {
                            is Number -> p.toDouble()
                            is String -> p.toDoubleOrNull() ?: 0.0
                            else -> 0.0
                        },
                        imageUrl = item["imageUrl"] as? String ?: "",
                        quantity = when (val q = item["quantity"]) {
                            is Number -> q.toInt()
                            is String -> q.toIntOrNull() ?: 0
                            else -> 0
                        }
                    )
                }

                Order(
                    id = document.getString("id") ?: document.id,
                    userId = document.getString("userId") ?: "",
                    userEmail = document.getString("userEmail") ?: "",
                    items = orderItems,
                    subtotal = when (val s = document.get("subtotal")) {
                        is Number -> s.toDouble()
                        is String -> s.toDoubleOrNull() ?: 0.0
                        else -> 0.0
                    },
                    tax = when (val t = document.get("tax")) {
                        is Number -> t.toDouble()
                        is String -> t.toDoubleOrNull() ?: 0.0
                        else -> 0.0
                    },
                    total = when (val tot = document.get("total")) {
                        is Number -> tot.toDouble()
                        is String -> tot.toDoubleOrNull() ?: 0.0
                        else -> 0.0
                    },
                    status = document.getString("status") ?: "PENDIENTE",
                    date = when (val d = document.get("date")) {
                        is Timestamp -> d.toDate().time
                        is Number -> d.toLong()
                        is String -> d.toLongOrNull() ?: 0L
                        else -> 0L
                    }
                )
            } catch (e: Exception) {
                null
            }
        }.sortedByDescending { it.date }
    }
}