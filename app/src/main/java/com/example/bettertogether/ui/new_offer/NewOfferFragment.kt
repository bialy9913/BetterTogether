package com.example.bettertogether.ui.new_offer

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment

import com.example.bettertogether.R
import com.example.bettertogether.databinding.FragmentNewOfferBinding
import com.example.bettertogether.ui.base.BaseFragment
import com.example.bettertogether.utils.hide
import com.example.bettertogether.utils.info
import com.example.bettertogether.utils.show
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import org.json.JSONArray
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewOfferFragment : BaseFragment(),NewOfferNavigator {

    private val newOfferViewModel: NewOfferViewModel by viewModel()
    private lateinit var binding : FragmentNewOfferBinding
    private val navController by lazy { NavHostFragment.findNavController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newOfferViewModel.setNavigator(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_new_offer, container, false
            )
        binding.newOfferViewModel=newOfferViewModel
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && (requestCode==100 || requestCode==200)) {
            val feature = PlaceAutocomplete.getPlace(data)
            var jsonArray= JSONObject(feature.geometry()!!.toJson()).get("coordinates") as JSONArray
            if(requestCode==100){
                newOfferViewModel.startPoint.set(feature.placeName().toString())
                newOfferViewModel.startPoint.notifyChange()
                binding.newOfferViewModel!!.offer.startPointLat = jsonArray.get(1) as Double
                binding.newOfferViewModel!!.offer.startPointLng = jsonArray.get(0) as Double
            }
            else if(requestCode==200){
                newOfferViewModel.endPoint.set(feature.placeName().toString())
                newOfferViewModel.endPoint.notifyChange()
                binding.newOfferViewModel!!.offer.endPointLat = jsonArray.get(1) as Double
                binding.newOfferViewModel!!.offer.endPointLng = jsonArray.get(0) as Double
            }
        }
    }

    override fun onStartPointOnClick() {
        var placeOption = PlaceOptions.builder()
            .backgroundColor(Color.parseColor("#EEEEEE"))
            .build(PlaceOptions.MODE_CARDS)
        val intent = PlaceAutocomplete.IntentBuilder()
            .accessToken("pk.eyJ1IjoidGVnIiwiYSI6ImNraTZmdXBtbTAzMmIycnMwbDc2MXhsazMifQ.rYMJuEyuKXruT2l23Mg1NA")
            .placeOptions(placeOption)
            .build(this.activity)
        startActivityForResult(intent, 100)
    }

    override fun onEndPointOnClick() {
        var placeOption = PlaceOptions.builder()
            .backgroundColor(Color.parseColor("#EEEEEE"))
            .build(PlaceOptions.MODE_CARDS)
        val intent = PlaceAutocomplete.IntentBuilder()
            .accessToken("pk.eyJ1IjoidGVnIiwiYSI6ImNraTZmdXBtbTAzMmIycnMwbDc2MXhsazMifQ.rYMJuEyuKXruT2l23Mg1NA")
            .placeOptions(placeOption)
            .build(this.activity)
        startActivityForResult(intent, 200)
    }

    override fun addNewOfferStarted() {
        binding.pbNewOffer.show()
    }

    override fun addNewOfferSuccess() {
        binding.pbNewOffer.hide()
        showToast("Entire Success")
        clearFields()
    }

    override fun addNewOfferFailure(message: String?) {
        binding.pbNewOffer.hide()
        showToast(message!!)
    }

    private fun clearFields(){
        newOfferViewModel.startPoint.set(null)
        newOfferViewModel.startPoint.notifyChange()
        newOfferViewModel.endPoint.set(null)
        newOfferViewModel.endPoint.notifyChange()
        binding.etNewOfferPrice.text?.clear()
        binding.tietStartPoint.requestFocus()
        binding.etSeatNumber.text.clear()
        binding.etAdditionalComment.text.clear()
    }

}
