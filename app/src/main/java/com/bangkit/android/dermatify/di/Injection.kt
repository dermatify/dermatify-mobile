package com.bangkit.android.dermatify.di

import android.content.Context
import com.bangkit.android.dermatify.data.local.UserPreferences
import com.bangkit.android.dermatify.data.local.userDataStore
import com.bangkit.android.dermatify.data.remote.retrofit.ApiConfig
import com.bangkit.android.dermatify.data.repository.UserRepository

object Injection {
    fun provideAuthRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val userDataStore = UserPreferences.getInstance(context.userDataStore)
        return UserRepository.getInstance(apiService, userDataStore)
    }
}