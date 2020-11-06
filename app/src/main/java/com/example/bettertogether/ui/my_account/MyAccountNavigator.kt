package com.example.bettertogether.ui.my_account

import com.example.bettertogether.ui.base.BaseNavigator

interface MyAccountNavigator :BaseNavigator{
    fun navigateToAuth()
    fun fetchingCurrOffersStarted()
    fun fetchingCurrOffersSuccess(currOfferNumber:Int)
    fun fetchingCurrOffersFailure(message:String?)
    fun cancelRideStarted()
    fun cancelRideSuccess()
    fun cancelRideFailure(message:String?)
}