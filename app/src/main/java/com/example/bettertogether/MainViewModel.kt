package com.example.bettertogether

import androidx.lifecycle.ViewModel
import com.example.bettertogether.repositories.auth.FirebaseRepository

class MainViewModel(
    private var firebaseRepository: FirebaseRepository

): ViewModel() {
    fun logOut(){
        firebaseRepository.logOut()
    }
}