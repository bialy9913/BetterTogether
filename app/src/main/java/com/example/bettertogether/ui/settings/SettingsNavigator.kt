package com.example.bettertogether.ui.settings

import com.example.bettertogether.ui.base.BaseNavigator

interface SettingsNavigator :BaseNavigator{
    fun onGetMaxDistanceStarted()
    fun onGetMaxDistanceSuccess(maxDistance:String)
    fun onGetMaxDistanceFailure(message:String?)
    fun onChangePasswordStarted()
    fun onChangePasswordSuccess()
    fun onChangePasswordFailure(message:String)
    fun onChangeMaxDistanceStarted()
    fun onChangeMaxDistanceSuccess()
    fun onChangeMaxDistanceFailure(message:String)
}