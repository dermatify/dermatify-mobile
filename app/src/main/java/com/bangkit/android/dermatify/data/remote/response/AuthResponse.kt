package com.bangkit.android.dermatify.data.remote.response

data class LoginResponse(
    val refreshToken: String,
    val accessToken: String
)

data class ErrorResponse(
    val statusCode: Int,
    val error: String,
    val message: String
)