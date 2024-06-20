package com.bangkit.android.dermatify.di

import android.content.Context
import android.util.Log
import com.bangkit.android.dermatify.data.local.preferences.UserPreferences
import com.bangkit.android.dermatify.data.local.preferences.userDataStore
import com.bangkit.android.dermatify.data.local.room.HistoryDatabase
import com.bangkit.android.dermatify.data.remote.retrofit.ApiConfig
import com.bangkit.android.dermatify.data.repository.ArticlesRepository
import com.bangkit.android.dermatify.data.repository.JourneyRepository
import com.bangkit.android.dermatify.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
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
        val apiService = ApiConfig.getArticlesApiService()
        return ArticlesRepository.getInstance(apiService)
    }

    fun provideRepository(context: Context): JourneyRepository {
        val database = HistoryDatabase.getInstance(context)
        val dao = database.scanHistoryDao()
        return JourneyRepository.getInstance(dao)
    }
}