package com.bangkit.android.dermatify.data.remote.response

sealed class ApiResponse<out T> private constructor() {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val errorMsg: String) : ApiResponse<Nothing>()
    object Loading: ApiResponse<Nothing>()
}