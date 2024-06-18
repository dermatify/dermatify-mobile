package com.bangkit.android.dermatify.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.MediatorLiveData
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.data.remote.response.ArticlesItem
import com.bangkit.android.dermatify.data.repository.ArticlesRepository
import com.bangkit.android.dermatify.data.repository.UserRepository
import com.bangkit.android.dermatify.di.Injection

class HomeViewModel(
    private val userRepository: UserRepository,
    private val articlesRepository: ArticlesRepository
) : ViewModel() {
    private val _articles = MediatorLiveData<ApiResponse<*>>()
    val articles: LiveData<ApiResponse<*>> = _articles

    init {
        fetchArticles()
    }
    fun fetchArticles() = _articles.addSource(articlesRepository.fetchArticles()) { response ->
        Log.d("Cilukba", "update result = $response")
        _articles.value = response
    }
    fun getUserName() = userRepository.getUserName().asLiveData()

    fun getUserPic() = userRepository.getUserPic().asLiveData()
}

class ViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val articlesRepository: ArticlesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel:: class.java)) {
            return HomeViewModel(userRepository, articlesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideAuthRepository(context),
                    Injection.provideArticlesRepository()
                )
            }.also { instance = it }
    }
}