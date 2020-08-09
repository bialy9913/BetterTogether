package com.example.bettertogether.ui.home

import android.location.Location
import com.example.bettertogether.ui.base.BaseNavigator
import com.google.android.gms.maps.model.LatLng

interface HomeNavigator : BaseNavigator{
    fun onStarted()
    fun onSuccess(latLng: LatLng)
    fun onFailure(message:String)
    fun gettingCurrentLocationSuccess(location:Location?)
    fun gettingCurrentLocationFailure(message:String?)
    fun onSearchClick()
}