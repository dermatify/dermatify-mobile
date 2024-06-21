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
    fun insertHistory(history: Scans)

    @Query("SELECT * FROM ScansHistory")
    fun getAllHistories(): LiveData<List<Scans>>

}