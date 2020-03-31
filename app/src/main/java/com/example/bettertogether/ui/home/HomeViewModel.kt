package com.example.bettertogether.ui.home

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.viewModelScope
import com.example.bettertogether.ui.base.BaseViewModel
import com.example.bettertogether.utils.info
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import java.lang.Exception

class HomeViewModel : BaseViewModel<HomeNavigator>(){
    var searchLocation:String=""
    lateinit var homeFragmentContext:Context
    fun onSearchLocationImageClicked(){
        viewModelScope.launch {
            geolocate()
        }
    }
    suspend fun geolocate(){
        delay(1000L)
        navigator()?.onStarted()
        val geocoder=Geocoder(homeFragmentContext)
        var list: List<Address> = ArrayList()
        try{
            list = geocoder.getFromLocationName(searchLocation,1)
        }
        catch(e:Exception){
            navigator()?.onFailure("Geolocate found an exception: "+e.message.toString())
        }
        if(list.isNotEmpty()){
            info("Found a location: "+list[0].toString())
            navigator()?.onSuccess()
        }
        else{
            navigator()?.onFailure("No results found")
        }
    }
    suspend fun geolocate1(){
        navigator()?.onStarted()
        val geocoder=Geocoder(homeFragmentContext)
        var list: List<Address> = ArrayList()
        list = geocoder.getFromLocationName(searchLocation,1)
        if(list.isNotEmpty()){
            info("Found a location: "+list[0].toString())
            navigator()?.onSuccess()
        }
        else{
            navigator()?.onFailure("No results found")
        }
    }
    suspend fun geolocate2():List<Address>{
        val geocoder=Geocoder(homeFragmentContext)
        delay(1000L)
        return geocoder.getFromLocationName(searchLocation,1)
    }
}