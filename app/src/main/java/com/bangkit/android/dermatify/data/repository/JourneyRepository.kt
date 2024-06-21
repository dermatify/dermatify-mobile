package com.bangkit.android.dermatify.data.repository

import androidx.lifecycle.LiveData
import com.bangkit.android.dermatify.data.local.dao.ScanHistoryDao
import com.bangkit.android.dermatify.data.local.entity.Scans

class JourneyRepository private constructor(
    private val scanHistoryDao: ScanHistoryDao,
) {

    fun getAllHistories(): LiveData<List<Scans>> =
        scanHistoryDao.getAllHistories()

   fun getHistoriesByMonth(month: String): LiveData<List<Scans>> =
        scanHistoryDao.getHistoriesByMonth(month)

   fun getHistoriesByWeek(week: String): LiveData<List<Scans>> =
        scanHistoryDao.getHistoriesByWeek(week)

    suspend fun size(): Int = scanHistoryDao.getDbSize()

    fun addHistory(imageUri: String, description: String, timestamp: String, diagnosis: String) {
        val scan = Scans(
            imageUri = imageUri,
            description = description,
            timestamp = timestamp,
            diagnosis = diagnosis
        )
//        scanHistoryDao.insertHistory(scan)
    }

    companion object {
        private var instance: JourneyRepository? = null

        fun getInstance(
            scanHistoryDao: ScanHistoryDao
        ): JourneyRepository =
            instance ?: synchronized(this) {
                instance ?: JourneyRepository(scanHistoryDao)
            }.also { instance = it }
    }

}