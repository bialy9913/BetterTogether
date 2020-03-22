package com.example.bettertogether.ui.auth.sign_up

import androidx.lifecycle.viewModelScope
import com.example.bettertogether.repositories.auth.FirebaseRepository
import com.example.bettertogether.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<SignUpNavigator>() {

    fun signUp(email:String,password:String){
        viewModelScope.launch {
            firebaseRepository.signUp(email, password, onSuccess = {
                navigator()?.signingUpSuccess()
            }, onFailure = {message ->
                navigator()?.signingUpFailure(message.toString())
            })
        }
    }
}