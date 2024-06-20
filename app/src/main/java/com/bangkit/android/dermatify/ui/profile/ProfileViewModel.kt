package com.bangkit.android.dermatify.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.data.repository.UserRepository
import com.bangkit.android.dermatify.di.Injection

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _logoutResponse = MediatorLiveData<ApiResponse<*>?>()
    val logoutResponse: LiveData<ApiResponse<*>?> = _logoutResponse

    private val _renewTokenResponse = MediatorLiveData<ApiResponse<*>?>()
    val renewTokenResponse: LiveData<ApiResponse<*>?> = _renewTokenResponse

    fun getEmail() = userRepository.getUserEmail().asLiveData()

    fun getUserPic() = userRepository.getUserPic().asLiveData()

    fun getUserName() = userRepository.getUserName().asLiveData()


    fun logout() {
        _logoutResponse.removeSource(userRepository.logout())
        _logoutResponse.addSource(userRepository.logout()) { response ->
            _logoutResponse.value = response
            _logoutResponse.value = null
        }
    }

    fun renewAccessToken() {
        _renewTokenResponse.removeSource(userRepository.renewAccessToken())
        _renewTokenResponse.addSource(userRepository.renewAccessToken()) { response ->
            _renewTokenResponse.value = response
            _renewTokenResponse.value = null
        }
    }


}

class ViewModelFactory private constructor(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userRepository) as T
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