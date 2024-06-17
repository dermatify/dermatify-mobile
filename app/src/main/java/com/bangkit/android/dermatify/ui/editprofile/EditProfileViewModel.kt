package com.bangkit.android.dermatify.ui.editprofile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.android.dermatify.data.repository.UserRepository
import com.bangkit.android.dermatify.di.Injection

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun saveUpdateName(newUserName: String) = userRepository.updateUserName(newUserName)
    fun saveUpdatePic(newUserPic: String) = userRepository.updateUserPic(newUserPic)
    fun updateUserProfileRemotely(name: String) = userRepository.updateUserProfile(name)
    fun renewAccessToken() = userRepository.renewAccessToken()

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
                instance ?: ViewModelFactory(Injection.provideAuthRepository(context))
            }.also { instance = it }
    }
}