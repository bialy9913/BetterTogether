package com.example.bettertogether.ui.home

import com.example.bettertogether.ui.base.BaseNavigator

interface HomeNavigator : BaseNavigator{
    fun onStarted()
    fun onSuccess()
    fun onFailure(message:String)
}