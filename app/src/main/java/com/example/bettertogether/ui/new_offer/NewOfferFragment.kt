package com.example.bettertogether.ui.new_offer

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
import com.example.bettertogether.utils.show
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
        binding.tietStartPoint.text?.clear()
        binding.tietEndPoint.text?.clear()
        binding.etNewOfferPrice.text?.clear()
        binding.tietStartPoint.requestFocus()
        binding.etSeatNumber.text.clear()
        binding.etAdditionalComment.text.clear()
    }

}
