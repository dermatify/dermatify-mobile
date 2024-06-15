package com.bangkit.android.dermatify.data.repository

import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.bangkit.android.dermatify.data.local.UserPreferences
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.data.remote.response.ErrorResponse
import com.bangkit.android.dermatify.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDataStore: UserPreferences
) {

    fun getUserEmail(): Flow<String> = userDataStore.getUserEmail()
    fun getAccessToken(): Flow<String> = userDataStore.getAccessToken()

    private fun getRefreshToken(): Flow<String> = userDataStore.getRefreshToken()

    private fun removeToken() {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStore.removeToken()
        }
    }

    fun login(email: String, password: String) = liveData {
        emit(ApiResponse.Loading)
        try {
            val successResponse = apiService.login(email, password)
            userDataStore.saveToken(successResponse.accessToken, successResponse.refreshToken)
            userDataStore.saveEmail(email)
            emit(ApiResponse.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.d("Cilukba", "${errorBody}")
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ApiResponse.Error(errorResponse.message))
        } catch (e: Exception) {
            Log.d("Cilukba", "${e.message}")
            val errorMessage = if (e.message?.contains("Unable to resolve host", ignoreCase = true) == true) {
                "Seems you lost your connection. Please try again"
            } else {
                "An error occurred. Please try again"
            }
            emit(ApiResponse.Error(errorMessage))
        }
    }

    fun logout() = liveData {
        emit(ApiResponse.Loading)
        try {
            val email = getUserEmail().first()
            val accessToken = getAccessToken().first()
            val successResponse = apiService.logout(email, accessToken)
            removeToken()
            emit(ApiResponse.Success(successResponse.message))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            Log.d("Cilukba", "$errorBody")
            emit(ApiResponse.Error(errorResponse.message))
        } catch (e: Exception) {
            Log.d("Cilukba", "${e.message}")
            val errorMessage = if (e.message?.contains("Unable to resolve host", ignoreCase = true) == true) {
                "Seems you lost your connection. Please try again"
            } else {
                "An error occurred. Please try again"
            }
            emit(ApiResponse.Error(errorMessage))
        }
    }

    fun register(name: String, email: String, password: String) = liveData {
        emit(ApiResponse.Loading)
        try {
            val successResponse = apiService.register(name, email, password)
            emit(ApiResponse.Success(successResponse.message))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.d("Cilukba", "$errorBody")
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ApiResponse.Error(errorResponse.message))
        } catch (e: Exception) {
            Log.d("Cilukba", "${e.message}")
            val errorMessage = if (e.message?.contains("Unable to resolve host", ignoreCase = true) == true) {
                "Seems you lost your connection. Please try again"
            } else {
                "An error occurred. Please try again"
            }
            emit(ApiResponse.Error(errorMessage))
        }
    }

    fun renewAccessToken() = liveData {
        emit(ApiResponse.Loading)
        try {
            val email = getUserEmail().first()
            val accessToken = getAccessToken().first()
            val successResponse = apiService.renewAccessToken(email, accessToken)
            userDataStore.updateAccessToken(successResponse.accessToken)
            emit(ApiResponse.Success(successResponse.message))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            val errorMsg = "${errorResponse.message} Logout"
            emit(ApiResponse.Error(errorMsg))
        } catch (e: Exception) {
            val errorMessage = if (e.message?.contains("Unable to resolve host", ignoreCase = true) == true) {
                "Seems you lost your connection. Please try again"
            } else {
                "An error occurred. Please try again"
            }
            emit(ApiResponse.Error(errorMessage))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(apiService: ApiService, userDataStore: UserPreferences): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDataStore)
            }.also { instance = it }
    }
}