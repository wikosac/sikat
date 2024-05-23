package com.rstj.sikat.src

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rstj.sikat.src.Injection.providePreferences
import com.rstj.sikat.src.Injection.provideRepository
import com.rstj.sikat.src.auth.AuthRepository
import com.rstj.sikat.src.auth.AuthViewModel

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
