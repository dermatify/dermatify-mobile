package com.bangkit.android.dermatify.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val loginSuccess: MutableLiveData<Boolean> =
        MutableLiveData()

    fun login(email: String, password: String) {
        val isLoggedIn = email.isNotBlank() && password.isNotBlank()
        loginSuccess.value = isLoggedIn
    }
}