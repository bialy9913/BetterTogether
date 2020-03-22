package com.example.bettertogether.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bettertogether.Coroutines
import com.example.bettertogether.repositories.auth.FirebaseRepository
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class AuthViewModel : ViewModel(), KoinComponent {
    private val firebaseRepository: FirebaseRepository by inject()
    lateinit var authListener: AuthListener

    val user by lazy {
        firebaseRepository.currentUser()
    }

    fun initAuthListener(authListener: AuthListener){
        this.authListener=authListener
    }


    fun logIn() {
        viewModelScope.launch {
            firebaseRepository.logIn2("dupa", "jasiu", onSuccess = {

            }, onFailure = {

            })
        }
    }

    fun logIn(email:String, password: String){
        authListener.onStarted()

        viewModelScope.launch {
            // sprobuj
        }

        Log.println(Log.INFO,"AuthViewModel/logIn","Starting of logging")
        Coroutines.main {
            Log.println(Log.INFO,"Coroutines.Main","Starting of logging")
            val response=firebaseRepository.logIn(email,password)
            Log.println(Log.INFO,"Coroutines.Main","here")
            if(response.isSuccessful){
                authListener.onSuccess()
            }
            else{
                authListener.onFailure(response.message.toString())
            }
        }
    }
    fun signUp(email:String,password:String){
        authListener.onStarted()
        Coroutines.main {
            val response=firebaseRepository.signUp(email,password)
            if(response.isSuccessful){
                authListener.onSuccess()
            }
            else{
                authListener.onFailure(response.message.toString())
            }
        }
    }
}