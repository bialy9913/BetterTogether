package com.example.bettertogether.ui.home


import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.bettertogether.Constants
import com.example.bettertogether.R
import com.example.bettertogether.databinding.FragmentHomeBinding
import com.example.bettertogether.ui.base.BaseFragment
import com.example.bettertogether.utils.debug
import com.example.bettertogether.utils.hide
import com.example.bettertogether.utils.show
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment(), HomeNavigator, OnMapReadyCallback {

    private val homeViewModel : HomeViewModel by viewModel()
    private lateinit var googleMap:GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.setNavigator(this)
        homeViewModel.homeFragmentContext=this.context as Context
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_home, container, false
            )
        binding.homeViewModel=homeViewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
    }
    override fun onStarted() {
        progressBar.show()
        inputSearch.text.clear()
    }

    override fun onSuccess() {
        progressBar.hide()
    }

    override fun onFailure(message:String) {
        progressBar.hide()
        showToast(message)
    }
    override fun onMapReady(map: GoogleMap) {
        googleMap=map
        debug("Getting devices current searchLocation")
        getDeviceLocation()
        googleMap.isMyLocationEnabled=true
    }
    private fun getDeviceLocation(){
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this.context as Context)
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {task ->
            if(task.isSuccessful){
                val currentLocation = task.result
                moveCamera(LatLng(currentLocation!!.latitude,currentLocation.longitude),Constants.DEFAULT_CAMERA_ZOOM)
            }
            else{
                showToast("Niestety blad")
            }
        }
    }
    private fun moveCamera(latLng: LatLng, zoom:Float){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom))
    }
}
