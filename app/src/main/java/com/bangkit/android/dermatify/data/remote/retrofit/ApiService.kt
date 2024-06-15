package com.bangkit.android.dermatify.data.remote.retrofit

import com.bangkit.android.dermatify.data.remote.response.LoginResponse
import com.bangkit.android.dermatify.data.remote.response.LogoutResponse
import com.bangkit.android.dermatify.data.remote.response.RegisterResponse
import com.bangkit.android.dermatify.data.remote.response.RenewResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("auth/logout")
    suspend fun logout(
        @Field("email") email: String,
        @Field("accessToken") accessToken: String
    ): LogoutResponse

    @FormUrlEncoded
    @POST("auth/renew")
    suspend fun renewAccessToken(
        @Field("email") email: String,
        @Field("refreshToken") refreshToken: String
    ): RenewResponse
}
