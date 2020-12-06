package com.example.bettertogether.ui.auth.sign_up

import com.example.bettertogether.ui.base.BaseNavigator

interface SignUpNavigator : BaseNavigator {
    fun signingUpStarted()
    fun signingUpSuccess()
    fun signingUpFailure(message:String)
    fun onDefaultStartPointClick()
}