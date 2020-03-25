package com.example.bettertogether.ui.auth.sign_in

import androidx.lifecycle.viewModelScope
import com.example.bettertogether.repositories.auth.FirebaseRepository
import com.example.bettertogether.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SignInViewModel(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel<SignInNavigator>() {

    fun logIn(email:String,password:String) {
        viewModelScope.launch {
            firebaseRepository.logIn(email, password
                ,onStarted = {
                    navigator()?.loggingInStarted()
                }
                , onSuccess = {
                navigator()?.loggingInSuccess()
            }, onFailure = {message ->
                navigator()?.loggingInFailure(message.toString())
            })
        }
    }
}