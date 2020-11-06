package com.example.bettertogether.ui.home

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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
import com.google.android.gms.maps.model.MarkerOptions
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
        binding.inputSearchS.text?.clear()
    }

    override fun onSuccess(latLng: LatLng) {
        moveCamera(latLng,Constants.DEFAULT_CAMERA_ZOOM,null)
    }

    override fun onFailure(message:String) {
        showToast(message)
    }

    override fun onChangeDirectionClick() {
        val tmp = binding.inputSearchS.text
        binding.inputSearchS.text=binding.inputSearchE.text
        binding.inputSearchE.text=tmp

    }

    override fun onSearchClick() {
        val bundle = bundleOf(
            "startPoint" to binding.inputSearchS.text.toString()
            ,"endPoint" to binding.inputSearchE.text.toString()
        )
        clearFields()
        navController.navigate(R.id.action_homeFragment_to_offersFragment,bundle)
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
        binding.inputSearchS.text.clear()
        binding.inputSearchE.text.clear()
    }
}
