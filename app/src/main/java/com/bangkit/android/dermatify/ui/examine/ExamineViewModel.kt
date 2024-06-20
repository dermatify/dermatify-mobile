package com.bangkit.android.dermatify.ui.examine

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.data.repository.UserRepository
import com.bangkit.android.dermatify.di.Injection
import java.io.File

class ExamineViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _analyzeReponse = MediatorLiveData<ApiResponse<*>?>()
    val analyzeReponse: LiveData<ApiResponse<*>?> = _analyzeReponse

    fun analyzePic(imageFile: File?) {
        if (imageFile != null) {
            _analyzeReponse.removeSource(userRepository.uploadPhoto(imageFile))
            _analyzeReponse.addSource(userRepository.uploadPhoto(imageFile)) { result ->
                Log.d("Cilukba", "analyze result = $result")
                _analyzeReponse.value = result
                _analyzeReponse.value = null
            }
        }
    }

}

class ViewModelFactory private constructor(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExamineViewModel::class.java)) {
            return ExamineViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideUserRepository(context)
                )
            }.also { instance = it }
    }
}