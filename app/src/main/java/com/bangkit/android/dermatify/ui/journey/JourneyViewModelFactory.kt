package com.bangkit.android.dermatify.ui.journey

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.android.dermatify.data.repository.JourneyRepository
import com.bangkit.android.dermatify.di.Injection

class JourneyViewModelFactory(private val repo: JourneyRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JourneyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JourneyViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var instance: JourneyViewModelFactory? = null

        fun getInstance(context: Context): JourneyViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: JourneyViewModelFactory(
                    Injection.provideRepository(context)
                )
            }.also { instance = it }
    }
}