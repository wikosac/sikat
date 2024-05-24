package com.rstj.sikat.src.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rstj.sikat.src.auth.AuthRepository
import com.rstj.sikat.src.auth.AuthViewModel
import com.rstj.sikat.src.utils.Injection.providePreferences
import com.rstj.sikat.src.utils.Injection.provideRepository

class ViewModelFactory(
    private val authRepository: AuthRepository,
    private val preferences: SettingPreferences
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepository, preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(provideRepository(), providePreferences(context))
        }.also { instance = it }
    }
}