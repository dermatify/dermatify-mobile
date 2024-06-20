package com.bangkit.android.dermatify.data.repository

import com.bangkit.android.dermatify.data.local.entity.Scans
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.ui.journey.JourneyDS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class JourneyRepository(
    private val dataStore: JourneyDS,
) {

    suspend fun getAllHistories(): Flow<ApiResponse<List<Scans>>> =
        dataStore.fetchAllHistories().flowOn(Dispatchers.IO)

    suspend fun getHistoriesByMonth(month: String): Flow<ApiResponse<List<Scans>>> =
        dataStore.fetchHistoriesByMonth(month).flowOn(Dispatchers.IO)

    suspend fun getHistoriesByWeek(week: String): Flow<ApiResponse<List<Scans>>> =
        dataStore.fetchHistoriesByWeek(week).flowOn(Dispatchers.IO)

    suspend fun addHistory(history: Scans) {
        dataStore.addHistory(history)
    }
}