package com.example.bettertogether.ui.settings


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
import com.example.bettertogether.utils.navigateWithoutComingBack
import com.example.bettertogether.utils.show
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    override fun onGetMaxDistanceStarted() {
        binding.firstProgressBar.show()
    }

    override fun onGetMaxDistanceSuccess(maxDistance:String) {
        binding.distanceDisplay.text=maxDistance
        binding.seekBar.progress=maxDistance.toInt()
        binding.firstProgressBar.hide()
        binding.maxDistanceLayout.show()
        binding.passwordChangeLayout.show()
    }

    override fun onGetMaxDistanceFailure(message: String?) {
        binding.firstProgressBar.hide()
        showToast("Unexpected error. Try to reopen settings.")
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
