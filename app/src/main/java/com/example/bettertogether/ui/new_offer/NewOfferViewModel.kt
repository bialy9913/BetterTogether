package com.example.bettertogether.ui.new_offer

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.example.bettertogether.models.CurrOffer
import com.example.bettertogether.models.Offer
import com.example.bettertogether.repositories.CurrentOffersRepository
import com.example.bettertogether.repositories.OfferRepository
import com.example.bettertogether.ui.base.BaseViewModel
import com.example.bettertogether.utils.convertToStringDate
import com.example.bettertogether.utils.info
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.time.LocalDate

class NewOfferViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val offerRepository:OfferRepository,
    private val currentOffersRepository: CurrentOffersRepository
) : BaseViewModel<NewOfferNavigator>() {

    @RequiresApi(Build.VERSION_CODES.O)
    var day = LocalDate.now().dayOfMonth
    @RequiresApi(Build.VERSION_CODES.O)
    var month = LocalDate.now().monthValue
    @RequiresApi(Build.VERSION_CODES.O)
    var year = LocalDate.now().year

    var startPoint= ObservableField<String>()
    var endPoint= ObservableField<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    val offer = Offer("","","","","","",0.0,0.0,
        "",0.0,0.0,"","","","",
        listOf())

    fun onStartPointOnClick(){
        navigator()?.onStartPointOnClick()
    }

    fun onEndPointOnClick(){
        navigator()?.onEndPointOnClick()
    }

    private suspend fun addOffer(currOffer:CurrOffer){
        viewModelScope.launch {
            offerRepository.addOffer(
                offer
                ,currOffer
                ,onStarted = {
                    navigator()?.addNewOfferStarted()
                }
                ,onSuccess = {

                    navigator()?.addNewOfferSuccess()
                }
                ,onFailure = {message ->
                    navigator()?.addNewOfferFailure(message)
                })
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun addNewOfferOnClick(){
        if(startPoint.get()!=null
            && endPoint.get()!=null
            && offer.ridePrice!!.isNotEmpty()
            && offer.seatNumber!!.isNotEmpty()
        ){
            offer.rideDate=convertToStringDate(day,month,year)
            offer.startPoint=startPoint.get()
            offer.endPoint=endPoint.get()
            val currOffer = CurrOffer("","",firebaseAuth.currentUser!!.uid,offer.startPoint,offer.endPoint,offer.ridePrice,offer.rideDate,offer.additionalComment,"","",offer.userRating,offer.seatNumber,offer.passengerList,"DRIVER","IN PROGRESS")
            navigator()?.addNewOfferStarted()
            viewModelScope.launch {
                info("Będę dodawał aktualna oferte dla kierowcy")
                info("Teraz dodaję aktualna oferte dla kierowcy")
                currentOffersRepository.addOffer(
                    currOffer
                    ,onStarted = {

                    }
                    ,onSuccess = {
                        info("Teraz dodaję aktualna oferte")
                        viewModelScope.launch { addOffer(currOffer) }
                    }
                    ,onFailure = {message ->
                        navigator()?.addNewOfferFailure(message)
                    })
            }
        }
    }
    fun onClick(view: View){
        info("asfasdfsdf")
    }
}
