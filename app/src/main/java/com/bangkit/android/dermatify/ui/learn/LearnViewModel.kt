package com.bangkit.android.dermatify.ui.learn

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.android.dermatify.data.remote.response.ApiResponse
import com.bangkit.android.dermatify.data.repository.ArticlesRepository
import com.bangkit.android.dermatify.di.Injection

class LearnViewModel(private val articlesRepository: ArticlesRepository) : ViewModel() {
    private val _articles = MediatorLiveData<ApiResponse<*>>()
    val articles: LiveData<ApiResponse<*>> = _articles

    init {
        fetchArticles()
    }

    fun fetchArticles() {
        _articles.removeSource(articlesRepository.fetchArticles())
        _articles.addSource(articlesRepository.fetchArticles()) { response ->
            Log.d("Cilukba", "fetch articles = $response")
            _articles.value = response
        }
    }
}

class ViewModelFactory private constructor(
    private val articlesRepository: ArticlesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LearnViewModel::class.java)) {
            return LearnViewModel(articlesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideArticlesRepository()
                )
            }.also { instance = it }
    }
}