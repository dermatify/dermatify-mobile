package com.bangkit.android.dermatify.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bangkit.android.dermatify.ui.login.LoginViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val ACCESS_TOKEN_KEY = stringPreferencesKey(LoginViewModel.ACCESS_TOKEN_KEY)
    private val REFRESH_TOKEN_KEY = stringPreferencesKey(LoginViewModel.REFRESH_TOKEN_KEY)
    private val USER_EMAIL_KEY = stringPreferencesKey(LoginViewModel.USER_EMAIL_KEY)



    fun getUserEmail(): Flow<String> {
        return dataStore.data.map { prefs ->
            prefs[USER_EMAIL_KEY] ?: ""
        }
    }

    fun getAccessToken(): Flow<String> {
        return dataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN_KEY] ?: ""
        }
    }

    fun getRefreshToken(): Flow<String> {
        return dataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN_KEY] ?: ""
        }
    }

    suspend fun removeToken() {
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
            prefs.remove(REFRESH_TOKEN_KEY)
        }
    }

    suspend fun updateAccessToken(newToken: String) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = newToken
        }
    }

    suspend fun saveToken(accessToken: String, refreshToken: String) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = accessToken
            prefs[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    suspend fun saveEmail(email: String) {
        dataStore.edit { prefs ->
            prefs[USER_EMAIL_KEY] = email
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}