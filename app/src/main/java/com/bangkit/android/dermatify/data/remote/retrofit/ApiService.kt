package com.bangkit.android.dermatify.data.remote.retrofit

import com.bangkit.android.dermatify.data.remote.response.AnalyzeResponse
import com.bangkit.android.dermatify.data.remote.response.ArticlesResponse
import com.bangkit.android.dermatify.data.remote.response.LoginResponse
import com.bangkit.android.dermatify.data.remote.response.LogoutResponse
import com.bangkit.android.dermatify.data.remote.response.RegisterResponse
import com.bangkit.android.dermatify.data.remote.response.RenewResponse
import com.bangkit.android.dermatify.data.remote.response.UpdateUserProfileResponse
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

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

    @POST("auth/logout")
    suspend fun logout(): LogoutResponse
    @POST("auth/renew")
    suspend fun renewAccessToken(): RenewResponse

    @FormUrlEncoded
    @PUT("user/profile")
    suspend fun updateUserProfile(
        @Field("name") name: String
    ): UpdateUserProfileResponse

    @GET("articles")
    suspend fun fetchArticles(): ArticlesResponse

    @Multipart
    @POST("analyze")
    suspend fun analyzeSkin(
        @Part image: MultipartBody.Part
    ): AnalyzeResponse
}
