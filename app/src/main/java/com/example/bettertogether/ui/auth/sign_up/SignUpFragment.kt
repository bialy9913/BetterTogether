package com.example.bettertogether.ui.auth.sign_up


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
import com.example.bettertogether.databinding.FragmentSignUpBinding
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


class SignUpFragment : BaseFragment(),SignUpNavigator {

    private val signUpViewModel: SignUpViewModel by viewModel()
    private val navController by lazy { NavHostFragment.findNavController(this) }
    private lateinit var binding: FragmentSignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpViewModel.setNavigator(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_sign_up, container, false
            )
        binding.signUpViewModel=signUpViewModel
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && (requestCode==100 || requestCode==200)) {
            val feature = PlaceAutocomplete.getPlace(data)
            var jsonArray= JSONObject(feature.geometry()!!.toJson()).get("coordinates") as JSONArray
            if(requestCode==100){
                signUpViewModel.defaultStartPoint.set(feature.placeName().toString())
                signUpViewModel.defaultStartPoint.notifyChange()
                signUpViewModel.defaultStartPointLat = jsonArray.get(1) as Double
                signUpViewModel.defaultStartPointLng= jsonArray.get(0) as Double
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
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

    override fun signingUpStarted() {
        clearFields()
        binding.progressBar.show()
    }

    override fun signingUpSuccess() {
        binding.progressBar.hide()
        showToast("Signing up success! Now you can log in")
        navController.navigateWithoutComingBack(R.id.signInFragment)
    }

    override fun signingUpFailure(message: String) {
        binding.progressBar.hide()
        showToast(message)
    }
    private fun clearFields(){
        binding.userName.text?.clear()
        binding.email.text?.clear()
        binding.password.text?.clear()
        binding.userName.error=null
        binding.email.error=null
        binding.password.error=null
        binding.userName.requestFocus()
        signUpViewModel.defaultStartPoint.set(null)
        signUpViewModel.defaultStartPoint.notifyChange()
    }
}
