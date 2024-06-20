package com.bangkit.android.dermatify.ui.journey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.android.dermatify.data.repository.JourneyRepository

class JourneyViewModelFactory(private val repo: JourneyRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JourneyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JourneyViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}