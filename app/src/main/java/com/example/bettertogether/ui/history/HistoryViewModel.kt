package com.example.bettertogether.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bettertogether.models.CurrOffer
import com.example.bettertogether.repositories.CurrentOffersRepository
import com.example.bettertogether.ui.base.BaseViewModel
import com.example.bettertogether.utils.displayPassengers
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val currentOffersRepository: CurrentOffersRepository
): BaseViewModel<HistoryNavigator>() {
    private val moffers = MutableLiveData<List<CurrOffer>>()
    val offers : LiveData<List<CurrOffer>>
        get() = moffers

    fun displayPassengerList(list:List<String>):String{
        return displayPassengers(list)
    }

    fun getCurrOffers(){
        navigator()?.fetchingCurrOffersStarted()
        viewModelScope.launch {
            moffers.value=currentOffersRepository.getHistoryOffers(
                onStarted = {
                    navigator()?.fetchingCurrOffersStarted()
                }
                ,onSuccess = {
                    navigator()?.fetchingCurrOffersSuccess(it)
                }
                ,onFailure = {
                    navigator()?.fetchingCurrOffersFailure(it)
                }
            )
        }
    }
}