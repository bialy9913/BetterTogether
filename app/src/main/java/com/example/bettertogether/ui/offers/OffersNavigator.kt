package com.example.bettertogether.ui.offers

import com.example.bettertogether.models.Offer
import com.example.bettertogether.ui.base.BaseNavigator

interface OffersNavigator : BaseNavigator {
    fun userChoosedOffer(offer:Offer)
}