package com.example.bettertogether.ui.auth.sign_in

import com.example.bettertogether.ui.base.BaseNavigator

interface SignInNavigator : BaseNavigator {
    fun emailIsNull()
    fun passwordIsNull()
    fun emailNotValid()
    fun passwordNotValid()
    fun signingInStarted()
    fun signingInSuccess()
    fun signingInFailure(message:String)
    fun navigateToSignUp()
}