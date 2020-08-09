package com.example.bettertogether.ui.offers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bettertogether.models.Offer
import com.example.bettertogether.ui.base.BaseViewModel

class OffersViewModel: BaseViewModel<OffersNavigator>() {
    private val moffers = MutableLiveData<List<Offer>>()
    val offers : LiveData<List<Offer>>
        get() = moffers

    fun getMovies(){
        val listOffer = ArrayList<Offer>()
        listOffer.add(Offer("123","Ziomal","","",""))
        listOffer.add(Offer("12345","Ziomson12345","","",""))
        moffers.value=listOffer.toList()
    }
}