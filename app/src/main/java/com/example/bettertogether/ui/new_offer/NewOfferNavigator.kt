package com.example.bettertogether.ui.new_offer

import com.example.bettertogether.ui.base.BaseNavigator

interface NewOfferNavigator:BaseNavigator {
    fun addNewOfferStarted()
    fun addNewOfferSuccess()
    fun addNewOfferFailure(message: String?)
    fun onStartPointOnClick()
    fun onEndPointOnClick()
}