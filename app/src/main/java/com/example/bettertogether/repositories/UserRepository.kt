package com.example.bettertogether.repositories

import com.example.bettertogether.model.User
import com.example.bettertogether.utils.info
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    fun addUser(user: User, onSuccess: () -> Unit, onFailure: (String?) -> Unit){
        val dataToAdd = hashMapOf(Pair("name",user.name), Pair("maxDestinationDistance",user.maxDestinationDistance))
        firestore
            .collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .set(dataToAdd)
            .addOnSuccessListener {
                info("Success in UserRepository")
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it.message)
            }
    }

    suspend fun getMaxDistance(
        onStarted: () -> Unit,
        onSuccess: (maxDistance:String) -> Unit,
        onFailure: (String?) -> Unit){
        onStarted()
        firestore
            .collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    onSuccess(it.result!!.get("maxDestinationDistance").toString())
                }
                else{
                    onFailure(it.exception?.message)
                }
            }
            .await()
    }

    suspend fun changeMaxDistance(
        newDistance:String,
        onStarted: () -> Unit,
        onSuccess: () -> Unit,
        onFailure: (String?) -> Unit){
        onStarted()
        val documentReference = firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
        documentReference.update("maxDestinationDistance",newDistance)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    onSuccess()
                }
                else{
                    onFailure(it.exception!!.message)
                }
            }.await()
    }
}