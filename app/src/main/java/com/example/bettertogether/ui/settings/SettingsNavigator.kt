package com.example.bettertogether.ui.settings

import com.example.bettertogether.ui.base.BaseNavigator

interface SettingsNavigator :BaseNavigator{
    fun onDefaultStartPointClick()
    fun onGetDefaultSettingsStarted()
    fun onGetDefaultSettingsSuccess(maxDistance:String)
    fun onGetDefaultSettingsFailure(message:String?)
    fun onChangeDefaultStartPointStarted()
    fun onChangeDefaultStartPointSuccess()
    fun onChangeDefaultStartPointFailure(message:String)
    fun onChangePasswordStarted()
    fun onChangePasswordSuccess()
    fun onChangePasswordFailure(message:String)
    fun onChangeMaxDistanceStarted()
    fun onChangeMaxDistanceSuccess()
    fun onChangeMaxDistanceFailure(message:String)
}