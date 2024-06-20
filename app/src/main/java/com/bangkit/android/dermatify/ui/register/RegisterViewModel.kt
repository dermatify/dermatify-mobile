package com.bangkit.android.dermatify.ui.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.android.dermatify.data.repository.UserRepository
import com.bangkit.android.dermatify.di.Injection

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) = userRepository.register(name, email, password)
}

class RegViewModelFactory private constructor(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: RegViewModelFactory? = null
        fun getInstance(context: Context): RegViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: RegViewModelFactory(Injection.provideUserRepository(context))
            }.also { instance = it }

    }
}
