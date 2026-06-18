package edu.proyecto.expressfoodapp.utils

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import edu.proyecto.expressfoodapp.worker.SyncOrdersWorker

object SyncUtils {

    fun syncPendingOrders(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<SyncOrdersWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context)
            .enqueue(request)
    }
}