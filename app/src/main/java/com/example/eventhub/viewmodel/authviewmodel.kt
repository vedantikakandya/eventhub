package com.example.eventhub.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.eventhub.data.firebase.authfirebase
import com.example.eventhub.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class authviewmodel: ViewModel(){

    private val repository = authfirebase()

    val _loginsuccess= MutableStateFlow(false)
    val loginstate: StateFlow<Boolean> = _loginsuccess

    val _signupsuccess=MutableStateFlow(false)
    val signupstate: StateFlow<Boolean> = _signupsuccess

    fun login(email:String, password:String){
        repository.loginfirebase(email, password){success->
            _loginsuccess.value = true
        }
    }


    fun signup(user: User, password: String) {
        repository.siginupfrebase(user, password) {success->
            _signupsuccess.value = true
        }
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