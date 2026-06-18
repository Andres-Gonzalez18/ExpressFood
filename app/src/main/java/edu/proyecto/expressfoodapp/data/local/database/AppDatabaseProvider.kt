package edu.proyecto.expressfoodapp.data.local.database

import android.content.Context
import androidx.room.Room
import edu.proyecto.expressfoodapp.utils.Constants

object AppDatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                Constants.DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()

            INSTANCE = instance
            instance
        }
    }
}