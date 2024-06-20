package com.bangkit.android.dermatify.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bangkit.android.dermatify.data.local.dao.ScanHistoryDao
import com.bangkit.android.dermatify.data.local.entity.Scans

@Database(
    entities = [Scans::class],
    version = 1,
    exportSchema = false
)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun scanHistoryDao(): ScanHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: HistoryDatabase? = null

        fun getInstance(context: Context): HistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryDatabase::class.java,
                    "history_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}