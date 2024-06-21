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


    private val _historyResult = MutableLiveData<ApiResponse<List<Scans>>>()
    val historyResult: LiveData<ApiResponse<List<Scans>>>
        get() = _historyResult


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

}