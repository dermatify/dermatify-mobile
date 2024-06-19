package com.bangkit.android.dermatify.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bangkit.android.dermatify.data.local.dao.ScanHistoryDao
import com.bangkit.android.dermatify.data.md.ScansEntity

@Database(
    entities = [ScansEntity::class],
    version = 1,
    exportSchema = false
)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun getHistories(): ScanHistoryDao
}