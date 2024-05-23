package com.rstj.sikat.src

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val EMAIL_KEY = stringPreferencesKey("auth_email")

    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[EMAIL_KEY]
        }
    }

    suspend fun saveToken(email: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
        }
    }

    suspend fun deleteToken() {
        dataStore.edit { preferences ->
            preferences.remove(EMAIL_KEY)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}