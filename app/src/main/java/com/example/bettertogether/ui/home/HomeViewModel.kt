package com.example.bettertogether.ui.home

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.View
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.bettertogether.ui.base.BaseViewModel
import com.example.bettertogether.utils.info
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.collections.ArrayList

class HomeViewModel : BaseViewModel<HomeNavigator>(){
    var searchLocation:String=""

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

    fun geolocate(view: View){
        if(searchLocation != ""){
            val searchLocationCopy=searchLocation
            navigator()?.onStarted()
            val geocoder=Geocoder(view.context)
            var list: List<Address>
            viewModelScope.launch(Dispatchers.Main) {
                list = geocoder.getFromLocationName(searchLocationCopy,1)
                if(list.isNotEmpty()){
                    info("Found a location: "+list[0].toString())
                    navigator()?.onSuccess()
                }
                else{
                    navigator()?.onFailure("No results found")
                }
            }
        }
    }
}