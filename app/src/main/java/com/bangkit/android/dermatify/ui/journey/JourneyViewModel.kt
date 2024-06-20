package com.bangkit.android.dermatify.ui.journey

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.android.dermatify.data.local.entity.Scans
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.data.repository.JourneyRepository
import kotlinx.coroutines.launch

class JourneyViewModel(
    private val journeyRepository: JourneyRepository
) : ViewModel() {

    val historyResult: LiveData<ApiResponse<List<Scans>>> by lazy { _historyResult }
    private val _historyResult = MutableLiveData<ApiResponse<List<Scans>>>()

    val historyByMonth: LiveData<ApiResponse<List<Scans>>> by lazy { _historyByMonth }
    private val _historyByMonth = MutableLiveData<ApiResponse<List<Scans>>>()

    val historyByWeek: LiveData<ApiResponse<List<Scans>>> by lazy { _historyByWeek }
    private val _historyByWeek = MutableLiveData<ApiResponse<List<Scans>>>()

    fun getAllHistories() {
        viewModelScope.launch {
            journeyRepository.getAllHistories().collect {
                _historyResult.postValue(it)
            }
        }
    }

    fun getHistoriesByMonth(month: String) {
        viewModelScope.launch {
            journeyRepository.getHistoriesByMonth(month).collect {
                _historyByMonth.postValue(it)
            }
        }
    }

    fun getHistoriesByWeek(week: String) {
        viewModelScope.launch {
            journeyRepository.getHistoriesByWeek(week).collect {
                _historyByWeek.postValue(it)
            }
        }
    }

    fun addHistory(imageUri: String, description: String, timestamp: String, diagnosis: String) {
        viewModelScope.launch {
            val scan = Scans(
                id = 0,
                imageUri = imageUri,
                description = description,
                timestamp = timestamp,
                diagnosis = diagnosis
            )
            journeyRepository.addHistory(scan)
        }
    }
}