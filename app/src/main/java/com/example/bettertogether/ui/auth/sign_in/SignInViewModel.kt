package com.example.bettertogether.ui.auth.sign_in

import androidx.lifecycle.viewModelScope
import com.example.bettertogether.model.SignInCredentials
import com.example.bettertogether.repositories.auth.FirebaseRepository
import com.example.bettertogether.ui.base.BaseViewModel
import com.example.bettertogether.utils.info
import com.example.bettertogether.utils.validateEmail
import com.example.bettertogether.utils.validatePassword
import kotlinx.coroutines.*

class SignInViewModel(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<SignInNavigator>() {
    var signInCredentials = SignInCredentials("","")

    fun onSignInButtonClick(){
        if(signInCredentials.email.isEmpty()){
            navigator()?.emailIsNull()
            return
        }
        if(signInCredentials.password.isEmpty()){
            navigator()?.passwordIsNull()
            return
        }
        if(!validateEmail(signInCredentials.email)){
            navigator()?.emailNotValid()
            return
        }
        if(!validatePassword(signInCredentials.password)){
            navigator()?.passwordNotValid()
            return
        }
        logIn()
    }
    fun onSignUpTextClick(){
        navigator()?.navigateToSignUp()
    }
    private fun logIn() {
        info("jestem "+viewModelScope.isActive)
        viewModelScope.launch(Dispatchers.Main) {
            info("here")
            firebaseRepository.signIn(
                signInCredentials.email
                ,signInCredentials.password
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