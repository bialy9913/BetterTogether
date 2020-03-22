package com.example.bettertogether.repositories.auth

import android.util.Log
import com.example.bettertogether.data.responses.AuthResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseRepository(private val firebaseAuth: FirebaseAuth) {


    suspend fun logIn(
        email:String, password: String,
        onSuccess: () -> Unit, onFailure: (String?) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
           if (it.isSuccessful) {
               onSuccess()
           } else {
               onFailure(it.exception?.message.toString())
           }
        }.await()
    }
    suspend fun signUp(
        email:String, password: String,
        onSuccess: () -> Unit, onFailure: (String?) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                onFailure(it.exception?.message.toString())
            }
        }.await()
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser
}