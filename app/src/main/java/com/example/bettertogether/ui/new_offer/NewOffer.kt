package com.example.bettertogether.ui.new_offer

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment

import com.example.bettertogether.R
import com.example.bettertogether.databinding.FragmentNewOfferBinding
import com.example.bettertogether.databinding.FragmentOffersBinding
import com.example.bettertogether.ui.base.BaseFragment
import com.example.bettertogether.ui.offers.OffersViewModel
import kotlinx.android.synthetic.main.fragment_new_offer.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewOffer : BaseFragment(),NewOfferNavigator {

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addNewOfferSuccess() {
        showToast("Entire Success")
        clearFields()
    }

    override fun addNewOfferFailure(message: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun clearFields(){
        binding.tietStartPoint.text?.clear()
        binding.tietEndPoint.text?.clear()
        binding.etNewOfferPrice.text?.clear()
        binding.tietStartPoint.requestFocus()
    }

}
