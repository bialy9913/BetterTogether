package com.example.bettertogether.ui.offers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bettertogether.models.CurrOffer
import com.example.bettertogether.models.Offer
import com.example.bettertogether.repositories.CurrentOffersRepository
import com.example.bettertogether.repositories.OfferRepository
import com.example.bettertogether.repositories.UserRepository
import com.example.bettertogether.ui.base.BaseViewModel
import com.example.bettertogether.utils.info
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class OffersViewModel(
    private val firebaseAuth: FirebaseAuth
    ,private val userRepository: UserRepository
    ,private val offerRepository: OfferRepository
    , private val currentOffersRepository: CurrentOffersRepository
): BaseViewModel<OffersNavigator>() {
    private val moffers = MutableLiveData<List<Offer>>()
    val offers : LiveData<List<Offer>>
        get() = moffers

    fun getOffers(startPoint:String,endPoint:String){
        viewModelScope.launch {
            moffers.value=offerRepository.getOffers(
                startPoint
                ,endPoint
                ,""
                ,onStarted = {
                    navigator()?.fetchingOffersStarted()
                }
                ,onSuccess = {offersNumber ->
                    navigator()?.fetchingOffersSuccess(offersNumber)
                }
                ,onFailure = {message ->
                    navigator()?.fetchingOffersFailure(message!!)
                })
        }
    }

    private fun addPassengerToDriverList(offer:Offer){
        info("Będę updatowal oferte kierowcy")
        viewModelScope.launch {
            offerRepository.addPassengerToList(
                offer
                ,onSuccess = {
                    navigator()?.userChoosedOfferSuccess()
                }
                ,onFailure = {message ->
                    navigator()?.userChoosedOfferFailure(message)
                }
            )
        }
    }

    fun userChosedOffer(offer:Offer){
        info("Teraz dodaję aktualna oferte dla pasażera")
        val currOffer = CurrOffer("",offer.offerUID,firebaseAuth.currentUser!!.uid,offer.startPoint,offer.endPoint,offer.ridePrice,offer.rideDate,offer.additionalComment,offer.UID,offer.userName,offer.userRating,offer.seatNumber,offer.passengerList,"PASSENGER","IN PROGRESS")
        viewModelScope.launch {
            currentOffersRepository.addOffer(
                currOffer
                ,onStarted = {
                    navigator()?.userChoosedOfferStarted()
                }
                ,onSuccess = {
                    addPassengerToDriverList(offer)
                }
                ,onFailure = {message ->
                    navigator()?.userChoosedOfferFailure(message)
                })
        }
    }

}