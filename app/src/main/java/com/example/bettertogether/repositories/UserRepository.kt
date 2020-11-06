package com.example.bettertogether.repositories

import com.example.bettertogether.models.User
import com.example.bettertogether.utils.info
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun addUser(user: User, onSuccess: () -> Unit, onFailure: (String?) -> Unit){
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
            }.await()
    }

    suspend fun getUserName():String{
        var userName=""
        firestore
            .collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    userName = it.result!!.get("name").toString()
                }
                else{
                    //onFailure(it.exception?.message)
                }
            }
            .await()
        return userName
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