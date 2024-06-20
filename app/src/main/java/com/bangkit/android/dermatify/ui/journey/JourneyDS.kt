package com.bangkit.android.dermatify.ui.journey

import com.bangkit.android.dermatify.data.local.dao.ScanHistoryDao
import com.bangkit.android.dermatify.data.local.entity.Scans
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow

class JourneyDS(private val historyDao: ScanHistoryDao) {

    suspend fun fetchAllHistories(): Flow<ApiResponse<List<Scans>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = historyDao.getAllHistories()
                    emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun fetchHistoriesByMonth(month: String): Flow<ApiResponse<List<Scans>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = historyDao.getHistoriesByMonth(month)
                    emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun fetchHistoriesByWeek(week: String): Flow<ApiResponse<List<Scans>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = historyDao.getHistoriesByWeek(week)
                    emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun addHistory(scan: Scans) {
        historyDao.insertHistory(scan)
    }
}