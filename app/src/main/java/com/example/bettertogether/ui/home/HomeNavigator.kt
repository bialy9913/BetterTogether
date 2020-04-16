package com.example.bettertogether.ui.home

import android.location.Location
import com.example.bettertogether.ui.base.BaseNavigator

interface HomeNavigator : BaseNavigator{
    fun onStarted()
    fun onSuccess()
    fun onFailure(message:String)
    fun gettingCurrentLocationSuccess(location:Location?)
    fun gettingCurrentLocationFailure(message:String?)
}