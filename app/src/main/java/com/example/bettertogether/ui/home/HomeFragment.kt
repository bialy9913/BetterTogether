package com.example.bettertogether.ui.home

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.example.bettertogether.Constants
import com.example.bettertogether.databinding.FragmentHomeBinding
import com.example.bettertogether.ui.base.BaseFragment
import com.example.bettertogether.utils.convertToStringDate
import com.example.bettertogether.utils.hide
import com.example.bettertogether.utils.info
import com.example.bettertogether.utils.show
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.gms.common.api.GoogleApiClient
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import org.json.JSONArray
import org.json.JSONObject

class HomeFragment : BaseFragment(), HomeNavigator, OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener {

    private val homeViewModel : HomeViewModel by viewModel()
    private val navController by lazy { NavHostFragment.findNavController(this) }
    private lateinit var binding: FragmentHomeBinding
    private lateinit var googleMap:GoogleMap


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.setNavigator(this)
        homeViewModel.setDefaultStartPoint()
        homeViewModel.moveOffersToHistory()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(
                inflater,
                com.example.bettertogether.R.layout.fragment_home, container, false
            )
        binding.homeViewModel=homeViewModel
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && (requestCode==100 || requestCode==200)) {
            val feature = PlaceAutocomplete.getPlace(data)
            var tmp = com.mapbox.mapboxsdk.geometry.LatLng(50.7833,22.2833)
            info(tmp.distanceTo(com.mapbox.mapboxsdk.geometry.LatLng(50.7592141,22.2781325)).toString())
            var jsonArray= JSONObject(feature.geometry()!!.toJson()).get("coordinates") as JSONArray
            if(requestCode==100){
                binding.homeViewModel!!.searchLocation.set(feature.placeName().toString())
                binding.homeViewModel!!.searchLocation.notifyChange()
                binding.homeViewModel!!.searchLocationLat = jsonArray.get(1) as Double
                binding.homeViewModel!!.searchLocationLng = jsonArray.get(0) as Double
            }
            else if(requestCode==200){
                binding.homeViewModel!!.searchLocationE.set(feature.placeName().toString())
                binding.homeViewModel!!.searchLocationE.notifyChange()
                binding.homeViewModel!!.searchLocationELat = jsonArray.get(1) as Double
                binding.homeViewModel!!.searchLocationELng = jsonArray.get(0) as Double
            }
        }
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.setDefaultStartPoint()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        binding.mapView.getMapAsync(this)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    /*override fun onStarted() {
        binding.inputSearchS.text?.clear()
    }

    override fun onSuccess(latLng: LatLng) {
        moveCamera(latLng,Constants.DEFAULT_CAMERA_ZOOM,null)
    }

    override fun onFailure(message:String) {
        showToast(message)
    }*/

    override fun onChangeDirectionClick() {
        val tmp = binding.inputSearchS.text
        binding.inputSearchS.text = binding.inputSearchE.text
        binding.inputSearchE.text=tmp
        val lat = homeViewModel.searchLocationLat
        val lng=homeViewModel.searchLocationLng
        homeViewModel.searchLocationLat=homeViewModel.searchLocationELat
        homeViewModel.searchLocationELat=lat
        homeViewModel.searchLocationLng=homeViewModel.searchLocationELng
        homeViewModel.searchLocationELng=lng
    }

    override fun onInputSearchClick() {
        var placeOption = PlaceOptions.builder()
            .backgroundColor(Color.parseColor("#EEEEEE"))
            .build(PlaceOptions.MODE_CARDS)
        val intent = PlaceAutocomplete.IntentBuilder()
            .accessToken("pk.eyJ1IjoidGVnIiwiYSI6ImNraTZmdXBtbTAzMmIycnMwbDc2MXhsazMifQ.rYMJuEyuKXruT2l23Mg1NA")
            .placeOptions(placeOption)
            .build(this.activity)
        startActivityForResult(intent, 100)
    }

    override fun onInputSearchEClick() {
        var placeOption = PlaceOptions.builder()
            .backgroundColor(Color.parseColor("#EEEEEE"))
            .build(PlaceOptions.MODE_CARDS)
        val intent = PlaceAutocomplete.IntentBuilder()
            .accessToken("pk.eyJ1IjoidGVnIiwiYSI6ImNraTZmdXBtbTAzMmIycnMwbDc2MXhsazMifQ.rYMJuEyuKXruT2l23Mg1NA")
            .placeOptions(placeOption)
            .build(this.activity)
        startActivityForResult(intent, 200)
    }

    override fun onSearchStarted() {
        binding.pbSearch.show()
    }

    override fun onSearchSuccess(maxDistance: String) {
        binding.pbSearch.hide()
        val bundle = bundleOf(
            "startPointLat" to homeViewModel.searchLocationLat
            ,"startPointLng" to homeViewModel.searchLocationLng
            ,"endPointLat" to homeViewModel.searchLocationELat
            ,"endPointLng" to homeViewModel.searchLocationELng
            ,"rideDate" to convertToStringDate(homeViewModel.day,homeViewModel.month,homeViewModel.year)
            ,"maxDistance" to maxDistance
        )
        clearFields()
        navController.navigate(com.example.bettertogether.R.id.action_homeFragment_to_offersFragment,bundle)
    }

    override fun onSearchFailure(message: String?) {
        binding.pbSearch.hide()
        showToast("Error occurred getting max distance from location: $message")
    }

    override fun gettingCurrentLocationSuccess(location:Location?) {
        if(location!=null)
            moveCamera(LatLng(location!!.latitude,location.longitude),Constants.DEFAULT_CAMERA_ZOOM,"My Location")
    }

    override fun gettingCurrentLocationFailure(message: String?) {
        showToast(message.toString())
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap=map
        googleMap.isMyLocationEnabled=true
        homeViewModel.getDeviceLocation(this.context as Context)
    }

    private fun moveCamera(latLng: LatLng, zoom:Float,title:String?){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom))

        if(title!="My Location"){
            val options = MarkerOptions()
                .position(latLng)
                .title(null)
            googleMap.addMarker(options)
        }
    }
    private fun clearFields(){
        homeViewModel.searchLocation.set(null)
        homeViewModel.searchLocation.notifyChange()
        homeViewModel.searchLocationE.set(null)
        homeViewModel.searchLocationE.notifyChange()
        homeViewModel.searchWithMaxDistance=false
    }
}
