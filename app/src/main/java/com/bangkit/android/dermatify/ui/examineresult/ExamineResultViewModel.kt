package com.bangkit.android.dermatify.ui.examineresult

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bangkit.android.dermatify.data.repository.UserRepository
import com.bangkit.android.dermatify.di.Injection
import kotlinx.coroutines.launch

class ExamineResultViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun insertScans(img: String, diagnosis: String, desc: String, date: String) {
        viewModelScope.launch {
            userRepository.insertScans(img, diagnosis, desc, date)
        }
    }
}

class ViewModelFactory private constructor(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExamineResultViewModel::class.java)) {
            return ExamineResultViewModel(userRepository) as T

        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideUserRepository(context)
                )
            }.also { instance = it }
    }
}