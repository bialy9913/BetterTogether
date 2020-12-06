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
        var dataToAdd:HashMap<String,Any?>
        if(user.defaultStartPoint.isNullOrEmpty()){
            dataToAdd = hashMapOf("name" to user.name
                ,"maxDestinationDistance" to user.maxDestinationDistance
                ,"defaultStartPoint" to ""
                ,"defaultStartPointLat" to null
                ,"defaultStartPointLng" to null
            )
        }
        else{
            dataToAdd = hashMapOf("name" to user.name
                ,"maxDestinationDistance" to user.maxDestinationDistance
                ,"defaultStartPoint" to user.defaultStartPoint
                ,"defaultStartPointLat" to user.defaultStartPointLat
                ,"defaultStartPointLng" to user.defaultStartPointLng
            )
        }
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

    suspend fun getDefaultStartPoint(onStarted: () -> Unit,onSuccess: (defaultStartPointParam:String) -> Unit,onFailure: (String?) -> Unit){
        var defaultStartPoint=""
        onStarted()
        firestore
            .collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    defaultStartPoint = it.result!!.get("defaultStartPoint").toString()+";"+it.result!!.get("defaultStartPointLat").toString()+";"+it.result!!.get("defaultStartPointLng").toString()
                    onSuccess(defaultStartPoint)
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
    suspend fun changeDefaultStartPoint(
        newStartPoint:String,
        newStartPointLat:Double,
        newStartPointLng:Double,
        onStarted: () -> Unit,
        onSuccess: () -> Unit,
        onFailure: (String?) -> Unit){
        onStarted()
        val documentReference = firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
        val batch = firestore.batch()
        batch
            .update(documentReference,"defaultStartPoint",newStartPoint)
            .update(documentReference,"defaultStartPointLat",newStartPointLat)
            .update(documentReference,"defaultStartPointLng",newStartPointLng)
            .commit()
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