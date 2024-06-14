package com.bangkit.android.dermatify.util

import android.content.Context
import com.bangkit.android.dermatify.data.remote.retrofit.ApiConfig
import com.bangkit.android.dermatify.data.repository.AuthRepository

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getApiService()
        return AuthRepository.getInstance(apiService)
    }
}