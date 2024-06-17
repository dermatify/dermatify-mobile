package com.bangkit.android.dermatify.data.remote.response

data class LoginResponse(
    val name: String,
    val refreshToken: String,
    val accessToken: String
)

data class RegisterResponse(
    val message: String
)

data class LogoutResponse(
    val message: String
)

data class RenewResponse(
    val message: String,
    val accessToken: String
)

data class UpdateUserProfileResponse(
    val name: String
)

data class ErrorResponse(
    val statusCode: Int,
    val error: String,
    val message: String
)