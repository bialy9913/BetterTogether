package com.example.bettertogether.ui.auth.sign_in

import androidx.lifecycle.viewModelScope
import com.example.bettertogether.model.SignInCredentials
import com.example.bettertogether.repositories.auth.FirebaseRepository
import com.example.bettertogether.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SignInViewModel(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<SignInNavigator>() {
    var signInCredentials = SignInCredentials("","")
    fun onSignInButtonClick(){
        logIn()
    }
    fun onSignUpTextClick(){
        navigator()?.navigateToSignUp()
    }
    private fun logIn() {
        viewModelScope.launch {
            firebaseRepository.signIn(signInCredentials
                ,onStarted = {
                    navigator()?.signingInStarted()
                }
                , onSuccess = {
                navigator()?.signingInSuccess()
            }, onFailure = {message ->
                navigator()?.signingInFailure(message.toString())
            })
        }
    }
}