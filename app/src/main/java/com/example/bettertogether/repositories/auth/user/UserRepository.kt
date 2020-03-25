package com.example.bettertogether.repositories.auth.user

import com.example.bettertogether.repositories.models.User
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository(
    private val firestore: FirebaseFirestore
) {
    fun addUser(user: User, onSuccess: () -> Unit, onFailure: (String?) -> Unit){
        firestore
            .collection("users")
            .add(user)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it.message)
            }
    }
}