package com.bangkit.android.dermatify.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.android.dermatify.data.repository.AuthRepository
import com.bangkit.android.dermatify.util.Injection

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    // TODO: Implement the ViewModel
    val loginSuccess: MutableLiveData<Boolean> =
        MutableLiveData()

    fun login(email: String, password: String) = authRepository.login(email, password)
}

class ViewModelFactory private constructor(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideAuthRepository(context))
            }.also { instance = it }
    }
}