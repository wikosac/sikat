package com.rstj.sikat.src.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.rstj.sikat.src.utils.Resource
import com.rstj.sikat.src.utils.SettingPreferences
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val pref: SettingPreferences
) : ViewModel() {

    fun login(email: String, password: String): LiveData<Resource<AuthResult>> {
        return authRepository.login(email, password)
    }

    fun register(email: String, password: String): LiveData<Resource<AuthResult>> {
        return authRepository.register(email, password)
    }

    fun getTokenPref(): LiveData<String?> = pref.getToken().asLiveData()

    fun saveTokenPref(email: String) {
        viewModelScope.launch {
            pref.saveToken(email)
        }
    }

    fun deleteTokenPref() {
        viewModelScope.launch {
            pref.deleteToken()
        }
        authRepository.signOut()
    }

}