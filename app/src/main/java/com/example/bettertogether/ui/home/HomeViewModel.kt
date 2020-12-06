package com.example.bettertogether.ui.home

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.example.bettertogether.repositories.CurrentOffersRepository
import com.example.bettertogether.repositories.OfferRepository
import com.example.bettertogether.repositories.UserRepository
import com.example.bettertogether.ui.base.BaseViewModel
import com.example.bettertogether.utils.info
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import kotlin.collections.ArrayList

class HomeViewModel(
    private val userRepository: UserRepository,
    private val currentOffersRepository: CurrentOffersRepository
) : BaseViewModel<HomeNavigator>(){
    var searchLocation:ObservableField<String?> = ObservableField()
    var searchLocationE:ObservableField<String?> = ObservableField()
    var searchLocationLat:Double=0.0
    var searchLocationLng:Double=0.0
    var searchLocationELat:Double=0.0
    var searchLocationELng:Double=0.0
    var searchWithMaxDistance= false

    @RequiresApi(Build.VERSION_CODES.O)
    var day = LocalDate.now().dayOfMonth
    @RequiresApi(Build.VERSION_CODES.O)
    var month = LocalDate.now().monthValue
    @RequiresApi(Build.VERSION_CODES.O)
    var year = LocalDate.now().year

    fun onInputSearchClick(){
        navigator()?.onInputSearchClick()
    }
    fun onInputSearchEClick(){
        navigator()?.onInputSearchEClick()
    }

    fun setDefaultStartPoint(){
        var defaultStartPoint:List<String> = listOf()
        viewModelScope.launch {
            userRepository.getDefaultStartPoint(
                onStarted = {}
                ,onSuccess = {
                    defaultStartPoint=it.split(";")
                }
                ,onFailure = {

                }
            )
            info(defaultStartPoint.toString())
            if(!defaultStartPoint.isNullOrEmpty() && !defaultStartPoint[0].isNullOrEmpty()){
                searchLocation.set(defaultStartPoint[0])
                searchLocationLat=defaultStartPoint[1].toDouble()
                searchLocationLng=defaultStartPoint[2].toDouble()
            }
        }
    }

    private fun getMaxDistance(){
        viewModelScope.launch {
            userRepository.getMaxDistance(
                onStarted = {navigator()?.onSearchStarted()},
                onSuccess = {maxDistance ->
                    navigator()?.onSearchSuccess(maxDistance)
                },
                onFailure = {message -> navigator()?.onSearchFailure(message)}
            )
        }
    }

    fun onSearchButtonClick(){
        if(searchLocation.get()==null && searchLocationE.get()==null) return
        if(searchWithMaxDistance) {
            getMaxDistance()
        }
        else{
            navigator()?.onSearchSuccess("")
        }
    }

    fun onChangeDirectionClick(){
        navigator()?.onChangeDirectionClick()
    }

    fun getDeviceLocation(context:Context){
        val fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(context)
        viewModelScope.launch(Dispatchers.IO) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {task ->
                if(task.isSuccessful){
                    navigator()?.gettingCurrentLocationSuccess(task.result)
                }
                else{
                    navigator()?.gettingCurrentLocationFailure("Couldn't find current location")
                }
            }.await()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun moveOffersToHistory(){
        viewModelScope.launch {
            currentOffersRepository.moveOffersToHistory()
        }
    }

    /*fun geolocate(view: View){
        if(searchLocation != ""){
            val searchLocationCopy=searchLocation
            navigator()?.onStarted()
            val geocoder=Geocoder(view.context)
            var list: List<Address>
            viewModelScope.launch(Dispatchers.Main) {
                list = geocoder.getFromLocationName(searchLocationCopy,1)
                if(list.isNotEmpty()){
                    info("Found a location: "+list[0].toString())
                    navigator()?.onSuccess(LatLng(list[0].latitude,list[0].longitude))
                }
                else{
                    navigator()?.onFailure("No results found")
                }
            }
        }
    }*/
}