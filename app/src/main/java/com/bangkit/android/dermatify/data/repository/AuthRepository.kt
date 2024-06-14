package com.bangkit.android.dermatify.data.repository

import android.util.Log
import androidx.lifecycle.liveData
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.data.remote.response.ErrorResponse
import com.bangkit.android.dermatify.data.remote.response.LoginResponse
import com.bangkit.android.dermatify.data.remote.retrofit.ApiService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException

class AuthRepository private constructor(
    private val apiService: ApiService
)   {
    fun login(email: String, password: String) = liveData {
        emit(ApiResponse.Loading)
        try {
            val successResponse = apiService.login(email, password)
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

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(apiService: ApiService): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService)
            }.also { instance = it }
    }
}