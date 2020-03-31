package com.example.bettertogether.repositories.auth

import com.example.bettertogether.repositories.auth.user.UserRepository
import com.example.bettertogether.model.User
import com.example.bettertogether.utils.info
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class FirebaseRepository(
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: UserRepository) {

    suspend fun signIn(
        email:String,
        password:String,
        onStarted:()->Unit,onSuccess: () -> Unit, onFailure: (String?) -> Unit) {
        onStarted()
        try{
            firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        onSuccess()
                        info("success")
                    } else {
                        onFailure(it.exception?.message.toString())
                    }
                }.await()
        }catch(e:Exception){
            onFailure(e.message.toString())
        }
    }
    suspend fun signUp(
        email:String,
        password:String,
        onStarted:()->Unit,onSuccess: () -> Unit, onFailure: (String?) -> Unit) {
        onStarted()
        try{
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                if (it.isSuccessful) {
                    userRepository.addUser(
                        User(
                            it.result!!.user!!.uid,
                            "Imie"
                        ),onSuccess,onFailure)
                } else {
                    onFailure(it.exception?.message.toString())
                }
            }.await()
        }catch(e:Exception){
            onFailure(e.message.toString())
        }
    }

    fun logOut() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser
}