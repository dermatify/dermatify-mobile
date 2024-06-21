package com.bangkit.android.dermatify.data.local.dao

import androidx.lifecycle.LiveData
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
    fun getAllHistories(): LiveData<List<Scans>>

    @Query("SELECT * FROM ScansHistory WHERE id = :id")
    suspend fun getDetailHistory(id: Int): Scans

    @Query("SELECT * FROM ScansHistory WHERE strftime('%m', timestamp) = :month")
    fun getHistoriesByMonth(month: String): LiveData<List<Scans>>

    @Query("SELECT * FROM ScansHistory WHERE strftime('%W', timestamp) = :week")
    fun getHistoriesByWeek(week: String): LiveData<List<Scans>>

    @Query("SELECT COUNT(*) FROM ScansHistory")
    suspend fun getDbSize(): Int

    @Query("SELECT * FROM ScansHistory ORDER BY id DESC LIMIT 1")
    fun getRecentScans(): LiveData<Scans>

}