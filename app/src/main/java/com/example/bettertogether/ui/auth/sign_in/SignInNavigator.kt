package com.example.bettertogether.ui.auth.sign_in

import com.example.bettertogether.ui.base.BaseNavigator

interface SignInNavigator : BaseNavigator {
    fun loggingInStarted()
    fun loggingInSuccess()
    fun loggingInFailure(message:String)
}