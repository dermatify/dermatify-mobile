package com.bangkit.android.dermatify.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bangkit.android.dermatify.data.local.entity.ScansEntity

@Dao
interface ScanHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistory(history: ScansEntity)

    @Query("SELECT * FROM ScansHistory")
    suspend fun getAllHistories(): List<ScansEntity>

    @Query("SELECT * FROM ScansHistory WHERE id = :id")
    suspend fun getDetailHistory(id: Int): ScansEntity

    @Query("SELECT * FROM ScansHistory WHERE strftime('%Y', timestamp) = :year AND strftime('%m', timestamp) = :month")
    suspend fun getHistoriesByMonth(year: String, month: String): List<ScansEntity>

    @Query("SELECT * FROM ScansHistory WHERE strftime('%Y', timestamp) = :year AND strftime('%W', timestamp) = :week")
    suspend fun getHistoriesByWeek(year: String, week: String): List<ScansEntity>

}