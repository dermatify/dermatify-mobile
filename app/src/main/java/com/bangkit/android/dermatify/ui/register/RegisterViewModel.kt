package com.bangkit.android.dermatify.ui.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.android.dermatify.data.repository.AuthRepository
import com.bangkit.android.dermatify.ui.login.ViewModelFactory
import com.bangkit.android.dermatify.util.Injection

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) = authRepository.register(name, email, password)
}

class RegViewModelFactory private constructor(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: RegViewModelFactory? = null
        fun getInstance(context: Context): RegViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: RegViewModelFactory(Injection.provideAuthRepository(context))
            }.also { instance = it }

    }
}
