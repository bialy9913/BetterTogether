package com.example.bettertogether.ui.history

import com.example.bettertogether.ui.base.BaseNavigator

interface HistoryNavigator:BaseNavigator {
    fun fetchingCurrOffersStarted()
    fun fetchingCurrOffersSuccess(historyOfferNumber: Int)
    fun fetchingCurrOffersFailure(message:String?)
}