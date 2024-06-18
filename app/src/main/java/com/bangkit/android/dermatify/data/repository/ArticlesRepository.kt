package com.bangkit.android.dermatify.data.repository

import android.util.Log
import androidx.lifecycle.liveData
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.data.remote.response.ErrorResponse
import com.bangkit.android.dermatify.data.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException

class ArticlesRepository private constructor(
    private var apiService: ApiService
){

    fun fetchArticles() = liveData {
        emit(ApiResponse.Loading)
        try {
            val successResponse = apiService.fetchArticles()
            Log.d("Cilukba", "articles is fetched ${successResponse}")
            emit(ApiResponse.Success(successResponse.articles))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.d("Cilukba", "save ${errorBody}")
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ApiResponse.Error(errorResponse.message))
        } catch (e: Exception) {
            Log.d("Cilukba", "save update ${e.message}")
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
        private var instance: ArticlesRepository? = null
        fun getInstance(apiService: ApiService): ArticlesRepository =
            instance ?: synchronized(this) {
                instance ?: ArticlesRepository(apiService)
            }.also { instance = it }
    }

}