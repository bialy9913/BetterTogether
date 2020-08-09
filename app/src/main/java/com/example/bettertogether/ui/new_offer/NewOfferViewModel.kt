package com.example.bettertogether.ui.new_offer

import androidx.lifecycle.ViewModel
import com.example.bettertogether.models.Offer
import com.example.bettertogether.ui.base.BaseViewModel

class NewOfferViewModel : BaseViewModel<NewOfferNavigator>() {
    val offer = Offer("","","","","")

    fun addNewOfferOnClick(){
        navigator()?.addNewOfferSuccess()
    }
}
