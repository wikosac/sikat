package com.rstj.sikat.src.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import com.rstj.sikat.src.auth.AuthRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

object Injection {

    fun provideRepository(): AuthRepository {
        val firebaseAuth = FirebaseAuth.getInstance()
        return AuthRepository.getInstance(firebaseAuth)
    }

    fun providePreferences(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(context.dataStore)
    }
}