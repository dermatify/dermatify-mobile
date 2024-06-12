package com.bangkit.android.dermatify.ui.changelanguage

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.android.dermatify.data.local.SettingsPreferences
import kotlinx.coroutines.launch

class ChangeLanguageViewModel(private val prefs: SettingsPreferences) : ViewModel() {
    var currentLocale: String = "en"

    fun getCurrentLocale(): LiveData<String> {
        return prefs.getCurrentLocale().asLiveData()
    }

    fun updateLocale(locale: String) {
        viewModelScope.launch {
            prefs.updateCurrentLocale(locale)
        }
    }

    companion object {
        const val LOCALE_KEY = "locale_key"
    }
}

class ChangeLanguageViewModelFactory(
    private val settingsPreferences: SettingsPreferences
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChangeLanguageViewModel::class.java)) {
            return ChangeLanguageViewModel(settingsPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}