package edu.proyecto.expressfoodapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import edu.proyecto.expressfoodapp.data.local.database.AppDatabaseProvider
import kotlinx.coroutines.tasks.await
import org.json.JSONArray

class SyncOrdersWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val database = AppDatabaseProvider.getDatabase(applicationContext)
            val orderDao = database.orderDao()
            val firestore = FirebaseFirestore.getInstance()

            val currentTime = System.currentTimeMillis()
            val twentyFourHoursInMillis = 24 * 60 * 60 * 1000L
            val threshold = currentTime - twentyFourHoursInMillis

            val activeOrders = orderDao.getActiveOrders()
            activeOrders.forEach { order ->
                if (order.date < threshold) {
                    orderDao.cancelOrderLocally(order.id)
                }
            }

            val unsyncedOrders = orderDao.getUnsyncedOrders()

            unsyncedOrders.forEach { order ->

                val itemsArray = JSONArray(order.itemsJson)
                val itemsList = mutableListOf<HashMap<String, Any>>()

                for (i in 0 until itemsArray.length()) {
                    val obj = itemsArray.getJSONObject(i)

                    itemsList.add(
                        hashMapOf(
                            "productId" to obj.getString("productId"),
                            "productName" to obj.getString("productName"),
                            "productPrice" to obj.getDouble("productPrice"),
                            "imageUrl" to obj.getString("imageUrl"),
                            "quantity" to obj.getInt("quantity")
                        )
                    )
                }

                val orderData = hashMapOf(
                    "id" to order.id,
                    "userId" to order.userId,
                    "userEmail" to order.userEmail,
                    "items" to itemsList,
                    "subtotal" to order.subtotal,
                    "tax" to order.tax,
                    "total" to order.total,
                    "status" to order.status,
                    "date" to order.date
                )

                firestore.collection("orders")
                    .document(order.id)
                    .set(orderData)
                    .await()

                orderDao.markAsSynced(order.id)
            }

            Result.success()

        } catch (e: Exception) {
            Result.retry()
        }
    }
}