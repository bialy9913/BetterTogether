package com.example.bettertogether.repositories

import com.example.bettertogether.models.SignUpCredentials
import com.example.bettertogether.models.User
import com.example.bettertogether.utils.debug
import com.example.bettertogether.utils.info
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import kotlin.Exception

class AuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: UserRepository
) {

    suspend fun signIn(
        email:String,
        password:String,
        onStarted:()->Unit
        ,onSuccess: () -> Unit
        ,onFailure: (String?) -> Unit) {
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
                }
                .await()
        }catch(e:Exception){
            onFailure(e.message)
        }
    }

    suspend fun signUp(
        signUpCredentials: SignUpCredentials
        ,onStarted:()->Unit
        ,onSuccess: () -> Unit
        ,onFailure: (String?) -> Unit) {
        onStarted()
        try{
            firebaseAuth.createUserWithEmailAndPassword(signUpCredentials.email,signUpCredentials.password)
                .addOnSuccessListener {
                }
                .addOnFailureListener {
                    onFailure(it.message)
                }
                .await()
        }catch(e:Exception){
            onFailure(e.message)
        }
        userRepository.addUser(
            User(
                signUpCredentials.userName,
                500.toString(),
                signUpCredentials.defaultStartPoint,
                signUpCredentials.defaultStartPointLat,
                signUpCredentials.defaultStartPointLng

            ),onSuccess,onFailure)
    }

    suspend fun changePassword(
        oldPassword:String
        ,newPassword:String
        ,onStarted:()->Unit
        ,onSuccess: () -> Unit
        ,onFailure: (String?) -> Unit){
        onStarted()
        val user=firebaseAuth.currentUser
        val credential:AuthCredential=EmailAuthProvider.getCredential(user!!.email!!,oldPassword)
        try{
            user.reauthenticate(credential).addOnCompleteListener {task ->
                if(task.isSuccessful){
                    try{
                        user.updatePassword(newPassword).addOnCompleteListener {task->
                            if(task.isSuccessful){
                                onSuccess()
                            }
                            else{
                                onFailure("Unsuccessful password change: ${task.exception}")
                            }
                        }
                    }catch (e:Exception){
                        onFailure("Unsuccessful password change: ${e.message}")
                    }
                }
                else{
                    onFailure("Wrong current password")
                }
            }.await()
        }catch (e:Exception){
            onFailure("Wrong current password")
        }
    }

    fun logOut(){
        firebaseAuth.signOut()
    }
}