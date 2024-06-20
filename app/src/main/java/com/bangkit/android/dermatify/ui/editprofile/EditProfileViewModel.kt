package com.bangkit.android.dermatify.ui.editprofile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.data.repository.UserRepository
import com.bangkit.android.dermatify.di.Injection

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _updateProfileResponse = MediatorLiveData<ApiResponse<*>?>()
    val updateProfileResponse: LiveData<ApiResponse<*>?> = _updateProfileResponse

    private val _renewTokenResponse = MediatorLiveData<ApiResponse<*>?>()
    val renewTokenResponse: LiveData<ApiResponse<*>?> = _renewTokenResponse

    fun saveUpdateName(newUserName: String) = userRepository.updateUserName(newUserName)
    fun saveUpdatePic(newUserPic: String) = userRepository.updateUserPic(newUserPic)

    fun updateUserProfileRemotely(name: String) {
        _updateProfileResponse.removeSource(userRepository.updateUserProfile(name))
        _updateProfileResponse.addSource(userRepository.updateUserProfile(name)) { result ->
            Log.d("Cilukba", "update result = $result")
            _updateProfileResponse.value = result
            _updateProfileResponse.value = null
        }
    }

    fun renewAccessToken() {
        _renewTokenResponse.removeSource(userRepository.renewAccessToken())
        _renewTokenResponse.addSource(userRepository.renewAccessToken()) { result ->
            _renewTokenResponse.value = result
            _renewTokenResponse.value = null
        }
    }
}

class ViewModelFactory private constructor(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideUserRepository(context))
            }.also { instance = it }
    }
}