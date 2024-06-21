package com.bangkit.android.dermatify.data.repository

import androidx.lifecycle.LiveData
import com.bangkit.android.dermatify.data.local.dao.ScanHistoryDao
import com.bangkit.android.dermatify.data.local.entity.Scans

class JourneyRepository private constructor(
    private val scanHistoryDao: ScanHistoryDao,
) {

    fun getAllHistories(): LiveData<List<Scans>> =
        scanHistoryDao.getAllHistories()

    fun addHistory(scan: Scans) {
        scanHistoryDao.insertHistory(scan)
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