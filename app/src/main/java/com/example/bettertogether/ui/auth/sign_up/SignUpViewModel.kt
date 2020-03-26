package com.example.bettertogether.ui.auth.sign_up

import androidx.lifecycle.viewModelScope
import com.example.bettertogether.model.SignUpCredentials
import com.example.bettertogether.repositories.auth.FirebaseRepository
import com.example.bettertogether.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<SignUpNavigator>() {
    var signUpCredentials = SignUpCredentials("","")

    fun onSignUpButtonClick(){
        signUp()
    }

    private fun signUp(){
        viewModelScope.launch {
            firebaseRepository.signUp(signUpCredentials
                ,onStarted = {
                    navigator()?.signingUpStarted()
                }
                , onSuccess = {
                navigator()?.signingUpSuccess()
            }, onFailure = {message ->
                navigator()?.signingUpFailure(message.toString())
            })
        }
    }
}