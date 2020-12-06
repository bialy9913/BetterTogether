package com.example.bettertogether.ui.settings


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
import com.example.bettertogether.databinding.FragmentSettingsBinding
import com.example.bettertogether.ui.base.BaseFragment
import com.example.bettertogether.utils.hide
import com.example.bettertogether.utils.info
import com.example.bettertogether.utils.navigateWithoutComingBack
import com.example.bettertogether.utils.show
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import org.json.JSONArray
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment(),SettingsNavigator {

    private lateinit var binding: FragmentSettingsBinding
    private val settingsViewModel:SettingsViewModel by viewModel()
    private val navController by lazy { NavHostFragment.findNavController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsViewModel.setNavigator(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_settings, container, false
            )
        binding.settingsViewModel=settingsViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backArrow.setOnClickListener {
            navController.popBackStack(R.id.myAccountFragment,false)
        }
        settingsViewModel.getMaxDistance()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && (requestCode==100 || requestCode==200)) {
            val feature = PlaceAutocomplete.getPlace(data)
            var jsonArray= JSONObject(feature.geometry()!!.toJson()).get("coordinates") as JSONArray
            if(requestCode==100){
                settingsViewModel.newStartPoint.set(feature.placeName().toString())
                settingsViewModel.newStartPoint.notifyChange()
                settingsViewModel.newStartPointLat = jsonArray.get(1) as Double
                settingsViewModel.newStartPointLng = jsonArray.get(0) as Double
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    override fun onGetDefaultSettingsStarted() {
        binding.firstProgressBar.show()
    }

    override fun onGetDefaultSettingsSuccess(maxDistance:String) {
        binding.distanceDisplay.text=maxDistance
        binding.seekBar.progress=maxDistance.toInt()
        binding.firstProgessBarLayout.hide()
        binding.maxDistanceLayout.show()
        binding.passwordChangeLayout.show()
        binding.defaultStartPointLayout.show()
    }

    override fun onGetDefaultSettingsFailure(message: String?) {
        binding.firstProgressBar.hide()
        showToast("Unexpected error. Try to reopen settings.")
    }

    override fun onDefaultStartPointClick() {
        var placeOption = PlaceOptions.builder()
            .backgroundColor(Color.parseColor("#EEEEEE"))
            .build(PlaceOptions.MODE_CARDS)
        val intent = PlaceAutocomplete.IntentBuilder()
            .accessToken("pk.eyJ1IjoidGVnIiwiYSI6ImNraTZmdXBtbTAzMmIycnMwbDc2MXhsazMifQ.rYMJuEyuKXruT2l23Mg1NA")
            .placeOptions(placeOption)
            .build(this.activity)
        startActivityForResult(intent, 100)
    }

    override fun onChangeDefaultStartPointStarted() {
        binding.pbDefaultStartPoint.show()
    }

    override fun onChangeDefaultStartPointSuccess() {
        binding.pbDefaultStartPoint.hide()
        showToast("Default start point successfully changed")
    }

    override fun onChangeDefaultStartPointFailure(message: String) {
        showToast(message)
    }

    override fun onChangePasswordStarted() {
        binding.progressBar.show()
    }

    override fun onChangePasswordSuccess() {
        binding.progressBar.hide()
        showToast("Password change success. Please log in with new credentials")
        navController.navigateWithoutComingBack(R.id.auth_nav_graph)
    }

    override fun onChangePasswordFailure(message: String) {
        binding.progressBar.hide()
        clearFields()
        showToast(message)
    }

    override fun onChangeMaxDistanceStarted() {
        binding.progressBar2.show()
    }

    override fun onChangeMaxDistanceSuccess() {
        binding.progressBar2.hide()
        showToast("Max distance from destination successfully changed")
    }

    override fun onChangeMaxDistanceFailure(message: String) {
        binding.progressBar2.hide()
        showToast(message)
    }

    private fun clearFields(){
        binding.oldPassword.text?.clear()
        binding.newPassword.text?.clear()
        binding.oldPassword.error=null
        binding.newPassword.error=null
        binding.oldPassword.requestFocus()
    }
}
