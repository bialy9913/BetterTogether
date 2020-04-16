package com.example.bettertogether.ui.home

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.example.bettertogether.Constants
import com.example.bettertogether.R
import com.example.bettertogether.databinding.FragmentHomeBinding
import com.example.bettertogether.ui.base.BaseFragment
import com.example.bettertogether.utils.hide
import com.example.bettertogether.utils.show
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment(), HomeNavigator, OnMapReadyCallback {

    private val homeViewModel : HomeViewModel by viewModel()
    private val navController by lazy { NavHostFragment.findNavController(this) }
    private lateinit var binding: FragmentHomeBinding
    private lateinit var googleMap:GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.setNavigator(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_home, container, false
            )
        binding.homeViewModel=homeViewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        binding.mapView.getMapAsync(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    override fun onStarted() {
        binding.progressBar.show()
        binding.inputSearch.text?.clear()
    }

    override fun onSuccess() {
        binding.progressBar.hide()
    }

    override fun onFailure(message:String) {
        binding.progressBar.hide()
        showToast(message)
    }
    override fun gettingCurrentLocationSuccess(location:Location?) {
        if(location!=null)
            moveCamera(LatLng(location!!.latitude,location.longitude),Constants.DEFAULT_CAMERA_ZOOM)
    }

    override fun gettingCurrentLocationFailure(message: String?) {
        showToast(message.toString())
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap=map
        googleMap.isMyLocationEnabled=true
        homeViewModel.getDeviceLocation(this.context as Context)
    }

    private fun moveCamera(latLng: LatLng, zoom:Float){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom))
    }
}
