package com.example.bettertogether.ui.my_account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bettertogether.models.CurrOffer
import com.example.bettertogether.repositories.AuthRepository
import com.example.bettertogether.repositories.CurrentOffersRepository
import com.example.bettertogether.ui.base.BaseViewModel
import com.example.bettertogether.utils.displayPassengers
import com.example.bettertogether.utils.info
import kotlinx.coroutines.launch

class MyAccountViewModel(
    private val authRepository: AuthRepository
    ,private val currentOffersRepository: CurrentOffersRepository
) : BaseViewModel<MyAccountNavigator>() {
    private val moffers = MutableLiveData<List<CurrOffer>>()
    val offers : LiveData<List<CurrOffer>>
        get() = moffers

    fun displayPassengerList(list:List<String>):String{
        return displayPassengers(list)
    }

    fun getCurrOffers(){
        viewModelScope.launch {
            moffers.value=currentOffersRepository.getCurrOffers(
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

    fun cancelRideOnClick(currOffer:CurrOffer){
        info("Teraz usuwam przejazdy")
        viewModelScope.launch {
            currentOffersRepository.cancelRide(
                currOffer
                ,onStarted = {navigator()?.cancelRideStarted()}
                ,onSuccess = {navigator()?.cancelRideSuccess()}
                ,onFailure = {
                    navigator()?.cancelRideFailure(it)
                }
            )
        }
    }

    fun signOut(){
        viewModelScope.launch {
            val isDone=authRepository.logOut()
            info("done: $isDone")
            navigator()?.navigateToAuth()
        }
    }
}