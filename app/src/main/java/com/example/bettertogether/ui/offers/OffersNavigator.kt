package com.example.bettertogether.ui.offers

import com.example.bettertogether.models.Offer
import com.example.bettertogether.ui.base.BaseNavigator

interface OffersNavigator : BaseNavigator {
    fun userChoosedOfferStarted()
    fun userChoosedOfferSuccess()
    fun userChoosedOfferFailure(message:String?)
    fun fetchingOffersStarted()
    fun fetchingOffersSuccess(offersNumber:Int)
    fun fetchingOffersFailure(message:String)
}