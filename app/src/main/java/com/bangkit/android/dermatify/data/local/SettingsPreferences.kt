package com.bangkit.android.dermatify.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bangkit.android.dermatify.ui.changelanguage.ChangeLanguageViewModel.Companion.LOCALE_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class SettingsPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val LOCALE = stringPreferencesKey(LOCALE_KEY)

    fun getCurrentLocale(): Flow<String> {
        return dataStore.data.map { prefs ->
            prefs[LOCALE] ?: "en"
        }
    }

    suspend fun updateCurrentLocale(locale: String) {
        dataStore.edit { prefs ->
            prefs[LOCALE] = locale
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: SettingsPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingsPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingsPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}