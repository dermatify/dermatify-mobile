package com.bangkit.android.dermatify.di

import android.content.Context
import android.util.Log
import com.bangkit.android.dermatify.data.local.UserPreferences
import com.bangkit.android.dermatify.data.local.userDataStore
import com.bangkit.android.dermatify.data.remote.retrofit.ApiConfig
import com.bangkit.android.dermatify.data.repository.ArticlesRepository
import com.bangkit.android.dermatify.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideAuthRepository(context: Context): UserRepository {
        val userDataStore = UserPreferences.getInstance(context.userDataStore)
        val accessToken = runBlocking {
            userDataStore.getAccessToken().first()
        }
        Log.d("Cilukba", "injection at: $accessToken")
        val apiServiceAT = ApiConfig.getApiService(accessToken)

        val refreshToken = runBlocking {
            userDataStore.getRefreshToken().first()
        }
        val apiServiceRT = ApiConfig.getApiService(refreshToken)
        return UserRepository.getInstance(apiServiceAT, apiServiceRT, userDataStore)
    }

    fun provideArticlesRepository(): ArticlesRepository {
        val apiService = ApiConfig.getApiService("")
        return ArticlesRepository.getInstance(apiService)
    }
}