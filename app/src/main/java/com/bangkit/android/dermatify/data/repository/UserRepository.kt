package com.bangkit.android.dermatify.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.android.dermatify.data.local.dao.ScanHistoryDao
import com.bangkit.android.dermatify.data.local.entity.Scans
import com.bangkit.android.dermatify.data.local.preferences.UserPreferences
import com.bangkit.android.dermatify.data.remote.response.AnalyzeError
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.data.remote.response.ErrorResponse
import com.bangkit.android.dermatify.data.remote.response.RecentScansResult
import com.bangkit.android.dermatify.data.remote.retrofit.ApiConfig
import com.bangkit.android.dermatify.data.remote.retrofit.ApiService
import com.google.gson.Gson
import com.google.gson.JsonElement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File

class UserRepository private constructor(
    private var apiServiceAT: ApiService,
    private var apiServiceRT: ApiService,
    private val userDataStore: UserPreferences,
    private val scansDao: ScanHistoryDao
) {

    fun getUserName(): Flow<String> = userDataStore.getUserName()
    fun getUserPic(): Flow<String> = userDataStore.getUserPic()
    fun getUserEmail(): Flow<String> = userDataStore.getUserEmail()
    fun getAccessToken(): Flow<String> = userDataStore.getAccessToken()

    fun getRecent(): LiveData<Scans> = scansDao.getRecentScans()

    suspend fun getDbsize() = scansDao.getDbSize()

    suspend     fun insertScans (img: String, diagnosis: String, desc: String, date: String) {
        val scansEntity = Scans(
            imageUri = img,
            description = desc,
            diagnosis = diagnosis,
            timestamp = date)
        scansDao.insertHistory(scansEntity)
    }

    private fun removePic() {
        CoroutineScope(Dispatchers.IO).launch {
            userDataStore.removePic()
        }
    }
    fun getRecents(callback: (ApiResponse<List<RecentScansResult>>) -> Unit) {
        val call = apiServiceAT.fetchRecentScans()

        call.enqueue(object : Callback<List<RecentScansResult>> {
            override fun onResponse(call: Call<List<RecentScansResult>>, response: Response<List<RecentScansResult>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback(ApiResponse.Success(it))
                    } ?: callback(ApiResponse.Error("Response body is null"))
                } else {
                    callback(ApiResponse.Error("Response was not successful: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<RecentScansResult>>, t: Throwable) {
                callback(ApiResponse.Error("${t.message}"))
            }
        })
    }

    fun uploadPhoto(imageFile: File) = liveData {
        emit(ApiResponse.Loading)
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            imageFile.name,
            requestImageFile
        )

        try {
            val successReponse = apiServiceAT.analyzeSkin(multipartBody)
            emit(ApiResponse.Success(successReponse.data))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AnalyzeError::class.java)
            emit(ApiResponse.Error(errorResponse.message))
        } catch (e: Exception) {
            val errorMessage = if (e.message?.contains("Unable to resolve host", ignoreCase = true) == true) {
                "Seems you lost your connection. Please try again"
            } else {
                "An error occurred. Please try again"
            }
            emit(ApiResponse.Error(errorMessage))
        }

    }

    fun updateUserName(newName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val oldName = getUserName().first()
            if (newName != oldName && newName.isNotEmpty()) userDataStore.updateUserName(newName)
        }
    }
    fun updateUserPic(newPic: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val oldPic = getUserPic().first()
            if (newPic == "removed") removePic()
            else if (newPic != oldPic && newPic.isNotEmpty()) userDataStore.updateUserPic(newPic)
        }
    }

    fun saveUpdateProfile(newName: String = "", newProfilePic: String = "") {
        CoroutineScope(Dispatchers.IO).launch {
            if (newProfilePic == "removed") {
                removePic()
            } else if (newProfilePic.isNotEmpty()) {
                updateUserPic(newProfilePic)
            }

            if (newName.isNotEmpty()) updateUserName(newName)
        }
    }

    fun updateUserProfile(newName: String) = liveData {
        emit(ApiResponse.Loading)
        try {
            val successResponse = apiServiceAT.updateUserProfile(newName)
            emit(ApiResponse.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ApiResponse.Error(errorResponse.message))
        } catch (e: Exception) {
            val errorMessage = if (e.message?.contains("Unable to resolve host", ignoreCase = true) == true) {
                "Seems you lost your connection. Please try again"
            } else {
                "An error occurred. Please try again"
            }
            emit(ApiResponse.Error(errorMessage))
        }
    }
    fun login(email: String, password: String) = liveData {
        emit(ApiResponse.Loading)
        try {
            val successResponse = apiServiceAT.login(email, password)
            apiServiceAT = ApiConfig.getApiService(successResponse.accessToken)
            apiServiceRT = ApiConfig.getApiService(successResponse.refreshToken)
            userDataStore.saveToken(successResponse.accessToken, successResponse.refreshToken)
            userDataStore.saveUserName(successResponse.name)
            userDataStore.saveEmail(email)
            emit(ApiResponse.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ApiResponse.Error(errorResponse.message))
        } catch (e: Exception) {
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
            val successResponse = apiServiceAT.logout()
            emit(ApiResponse.Success(successResponse.message))
            userDataStore.removeAll()
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ApiResponse.Error(errorResponse.message))
        } catch (e: Exception) {
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
            val successResponse = apiServiceAT.register(name, email, password)
            emit(ApiResponse.Success(successResponse.message))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ApiResponse.Error(errorResponse.message))
        } catch (e: Exception) {
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
            val successResponse = apiServiceRT.renewAccessToken()
            apiServiceAT = ApiConfig.getApiService(successResponse.accessToken)
            userDataStore.updateAccessToken(successResponse.accessToken)
            emit(ApiResponse.Success(successResponse.message))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            val errorMsg = "${errorResponse.message} Logout"
            emit(ApiResponse.Error(errorMsg))
            userDataStore.removeAll()
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
        fun getInstance(apiServiceAT: ApiService, apiServiceRT: ApiService, userDataStore: UserPreferences, scansDao: ScanHistoryDao): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiServiceAT, apiServiceRT, userDataStore, scansDao)
            }.also { instance = it }
    }
}


