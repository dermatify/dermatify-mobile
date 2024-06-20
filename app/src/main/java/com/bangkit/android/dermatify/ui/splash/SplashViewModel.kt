package com.bangkit.android.dermatify.ui.splash

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.bangkit.android.dermatify.data.repository.UserRepository
import com.bangkit.android.dermatify.di.Injection

class SplashViewModel(private val userRepository: UserRepository) : ViewModel() {
    init {
        getAccessToken()
    }

    fun getAccessToken(): LiveData<String> = userRepository.getAccessToken().asLiveData()
}

class ViewModelFactory private constructor(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideUserRepository(context))
            }.also { instance = it }
    }
}