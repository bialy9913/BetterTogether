package com.example.bettertogether.ui.auth.sign_in

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.bettertogether.repositories.AuthRepository
import com.example.bettertogether.models.ValidateEmail
import com.example.bettertogether.models.ValidatePassword
import com.example.bettertogether.ui.base.BaseViewModel
import com.example.bettertogether.utils.info
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel(
    private val authRepository: AuthRepository
) : BaseViewModel<SignInNavigator>() {
    var validateEmail = ValidateEmail("", false)
    var validatePassword = ValidatePassword("",false)

    //E-mail address validation
    fun validateEmailAddress(s:CharSequence){
        if(s.isEmpty()){
            validateEmail.isValid=false
            validateEmail.setError(null)
        }
        else{
            if(Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()){
                validateEmail.isValid=true
                validateEmail.setError(null)

            }
            else{
                validateEmail.isValid=false
                validateEmail.setError("E-mail address is not valid")
            }
        }
    }
    fun validatePassword(s:CharSequence){
        if(s.isEmpty()){
            validatePassword.isValid=false
            validatePassword.setError(null)
        }
        else{
            if(com.example.bettertogether.utils.validatePassword(s.toString())){
                validatePassword.isValid=true
                validatePassword.setError(null)
            }
            else{
                validatePassword.isValid=false
                validatePassword.setError("Password must have at least 6 characters")
            }
        }
    }
    fun onSignInButtonClick(){
        if(validateEmail.isValid && validatePassword.isValid){
            logIn()
        }
    }
    fun onSignUpTextClick(){
        navigator()?.navigateToSignUp()
    }
    private fun logIn() {
        viewModelScope.launch(Dispatchers.Main) {
            info("here")
            authRepository.signIn(
                validateEmail.email
                ,validatePassword.password
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