package com.example.bettertogether.ui.auth.sign_up

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.bettertogether.models.ValidateEmail
import com.example.bettertogether.models.ValidatePassword
import com.example.bettertogether.repositories.AuthRepository
import com.example.bettertogether.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authRepository: AuthRepository
) : BaseViewModel<SignUpNavigator>() {
    var validateEmail = ValidateEmail("",false)
    var validatePassword = ValidatePassword("",false)
    var userName:String=""

    //E-mail address and password validation
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

    fun onSignUpButtonClick(){
        if(validateEmail.isValid && validatePassword.isValid && userName.isNotEmpty()){
            signUp()
        }
    }

    private fun signUp(){
        viewModelScope.launch {
            authRepository.signUp(
                validateEmail.email
                ,validatePassword.password
                ,userName
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