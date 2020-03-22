package com.example.bettertogether.repositories.auth

import android.util.Log
import com.example.bettertogether.data.responses.AuthResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseRepository(private val firebaseAuth: FirebaseAuth) {


    suspend fun logIn2(
        email:String, password: String,
        onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
           if (it.isSuccessful) {
               onSuccess()
           } else {
               onFailure(it.exception?.message.toString())
           }
        }.await()
    }

    suspend fun logIn(email:String, password: String): AuthResponse {
        val authResponse = AuthResponse(false,null)
        Log.println(Log.INFO,"FirebaseRepositor/logIn","Starting of logging")
        try{
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                if(it.isSuccessful){
                    authResponse.isSuccessful=true
                }
                else{
                    authResponse.message=it.exception?.message
                }
                Log.println(Log.INFO,"Coroutines.Main","End") // TODO wyjebać
            }.await()
        }catch(e:Exception){
            authResponse.message=e.message
        }
        return authResponse
    }

    suspend fun signUp(email: String, password: String): AuthResponse {
        val authResponse=AuthResponse(false,null)
        Log.println(Log.INFO,"FirebaseRepositor/logIn","Starting of logging")
        try{
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                if(it.isSuccessful){
                    authResponse.isSuccessful=true
                }
                else{
                    authResponse.message=it.exception?.message
                }
                Log.println(Log.INFO,"Coroutines.Main","End")
            }.await()
        }catch(e:Exception){
            authResponse.message=e.message
        }
        return authResponse
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser
}