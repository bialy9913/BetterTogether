package com.example.bettertogether.repositories.auth

import com.example.bettertogether.model.SignInCredentials
import com.example.bettertogether.model.SignUpCredentials
import com.example.bettertogether.repositories.auth.user.UserRepository
import com.example.bettertogether.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseRepository(
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: UserRepository) {


    suspend fun signIn(
        signInCredentials: SignInCredentials,
        onStarted:()->Unit,onSuccess: () -> Unit, onFailure: (String?) -> Unit) {
        onStarted()
        firebaseAuth.signInWithEmailAndPassword(signInCredentials.email,signInCredentials.password).addOnCompleteListener {
           if (it.isSuccessful) {
               onSuccess()
           } else {
               onFailure(it.exception?.message.toString())
           }
        }.await()
    }
    suspend fun signUp(
        signUpCredentials: SignUpCredentials,
        onStarted:()->Unit,onSuccess: () -> Unit, onFailure: (String?) -> Unit) {
        onStarted()
        firebaseAuth.createUserWithEmailAndPassword(signUpCredentials.email,signUpCredentials.password).addOnCompleteListener {
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
    }

    fun logOut() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser
}