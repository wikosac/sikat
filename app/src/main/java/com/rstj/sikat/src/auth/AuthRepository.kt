package com.rstj.sikat.src.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.rstj.sikat.src.Resource

class AuthRepository(private val firebaseAuth: FirebaseAuth) {

    fun login(email: String, password: String): LiveData<Resource<AuthResult>> {
        val result = MutableLiveData<Resource<AuthResult>>()
        result.value = Resource.Loading

        try {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.let {
                            result.value = Resource.Success(it)
                        } ?: run {
                            result.value = Resource.Error(Exception("AuthResult is null"))
                        }
                    } else {
                        task.exception?.let {
                            result.value = Resource.Error(it)
                        } ?: run {
                            result.value = Resource.Error(Exception("Unknown authentication error"))
                        }
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
            result.value = Resource.Error(e)
        }

        return result
    }

    fun register(email: String, password: String): LiveData<Resource<AuthResult>> {
        val result = MutableLiveData<Resource<AuthResult>>()
        result.value = Resource.Loading

        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.let {
                            result.value = Resource.Success(it)
                        } ?: run {
                            result.value = Resource.Error(Exception("AuthResult is null"))
                        }
                    } else {
                        task.exception?.let {
                            result.value = Resource.Error(it)
                        } ?: run {
                            result.value = Resource.Error(Exception("Unknown authentication error"))
                        }
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
            result.value = Resource.Error(e)
        }

        return result
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    companion object {
        const val TAG = "AuthRepository"
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            firebaseAuth: FirebaseAuth
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(firebaseAuth)
            }.also { instance = it }
    }
}