package com.bangkit.android.dermatify.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bangkit.android.dermatify.data.local.entity.Scans

@Dao
interface ScanHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistory(history: Scans)

    @Query("SELECT * FROM ScansHistory")
    suspend fun getAllHistories(): List<Scans>

    @Query("SELECT * FROM ScansHistory WHERE id = :id")
    suspend fun getDetailHistory(id: Int): Scans

    @Query("SELECT * FROM ScansHistory WHERE strftime('%m', timestamp) = :month")
    suspend fun getHistoriesByMonth(month: String): List<Scans>

    @Query("SELECT * FROM ScansHistory WHERE strftime('%W', timestamp) = :week")
    suspend fun getHistoriesByWeek(week: String): List<Scans>

}