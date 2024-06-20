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

    private var _size = MutableLiveData(0)
    val size: LiveData<Int> = _size

    private fun getDbSize() {
        viewModelScope.launch {
            _size.value = journeyRepository.size()
        }
    }

    private val _historyResult = MutableLiveData<ApiResponse<List<Scans>>>()
    val historyResult: LiveData<ApiResponse<List<Scans>>>
        get() = _historyResult

    private val _historyByMonth = MutableLiveData<ApiResponse<List<Scans>>>()
    val historyByMonth: LiveData<ApiResponse<List<Scans>>>
        get() = _historyByMonth

    private val _historyByWeek = MutableLiveData<ApiResponse<List<Scans>>>()
    val historyByWeek: LiveData<ApiResponse<List<Scans>>>
        get() = _historyByWeek


    fun getAllHistories(): LiveData<List<Scans>> {
        getDbSize()
        return journeyRepository.getAllHistories()
    }

    fun getHistoriesByMonth(month: String): LiveData<List<Scans>> {
        getDbSize()
        return journeyRepository.getHistoriesByMonth(month)
    }

    fun getHistoriesByWeek(week: String): LiveData<List<Scans>> {
        getDbSize()
        return journeyRepository.getHistoriesByWeek(week)
    }

    fun refreshData() {
        viewModelScope.launch {
            _historyResult.value = ApiResponse.Loading
            try {
                journeyRepository.getAllHistories().observeForever { histories ->
                    _historyResult.value = ApiResponse.Success(histories)
                }
            } catch (e: Exception) {
                _historyResult.value = ApiResponse.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun refreshDataByMonth(month: String) {
        viewModelScope.launch {
            _historyByMonth.value = ApiResponse.Loading
            try {
                journeyRepository.getHistoriesByMonth(month).observeForever { histories ->
                    _historyByMonth.value = ApiResponse.Success(histories)
                }
            } catch (e: Exception) {
                _historyByMonth.value = ApiResponse.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun refreshDataByWeek(week: String) {
        viewModelScope.launch {
            _historyByMonth.value = ApiResponse.Loading
            try {
                journeyRepository.getHistoriesByMonth(week).observeForever { histories ->
                    _historyByMonth.value = ApiResponse.Success(histories)
                }
            } catch (e: Exception) {
                _historyByMonth.value = ApiResponse.Error(e.message ?: "An error occurred")
            }
        }
    }


}