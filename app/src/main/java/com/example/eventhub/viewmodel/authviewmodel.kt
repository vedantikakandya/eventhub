package com.example.eventhub.viewmodel

import androidx.lifecycle.ViewModel
import com.example.eventhub.data.firebase.authfirebase
import com.example.eventhub.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class authviewmodel: ViewModel(){

    private val repository = authfirebase()

    val _loginsuccess= MutableStateFlow(false)
    val loginstate: StateFlow<Boolean> = _loginsuccess

    private val _signupsuccess=MutableStateFlow(false)
    val signupstate: StateFlow<Boolean> = _signupsuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun login(email:String, password:String){
        repository.loginfirebase(email, password){ success, error ->
            _loginsuccess.value = success
            _errorMessage.value = error
        }
    }


    fun signup(user: User, password: String) {
        repository.siginupfrebase(user, password) { success, error ->
            _signupsuccess.value = success
            _errorMessage.value = error
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
    fun resetLoginState() {
        _loginsuccess.value = false
    }

    fun resetSignupState() {
        _signupsuccess.value = false
    }
    fun checkUserLoggedIn(): Boolean {
        return repository.isuserlogged()
    }

    fun logout() {
        repository.logout()
    }

}