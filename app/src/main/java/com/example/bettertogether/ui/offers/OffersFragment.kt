package com.example.bettertogether.ui.offers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bettertogether.R
import com.example.bettertogether.databinding.FragmentOffersBinding
import com.example.bettertogether.ui.base.BaseFragment
import com.example.bettertogether.utils.hide
import com.example.bettertogether.utils.show
import kotlinx.android.synthetic.main.fragment_offers.*
import kotlinx.android.synthetic.main.recyclerview_offers.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class OffersFragment: BaseFragment(),OffersNavigator {

    private val offersViewModel: OffersViewModel by viewModel()
    private lateinit var binding : FragmentOffersBinding
    private val navController by lazy { NavHostFragment.findNavController(this) }
    private lateinit var startPoint:String
    private lateinit var endPoint:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        offersViewModel.setNavigator(this)
        startPoint = arguments!!.getString("startPoint")!!
        endPoint = arguments!!.getString("endPoint")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_offers, container, false
            )
        binding.offersViewModel=offersViewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        offersViewModel.getOffers(startPoint,endPoint)
        offersViewModel.offers.observe(viewLifecycleOwner, Observer { offers ->
            recycler_view_offers.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)
                it.adapter =
                    OffersAdapter(offers, offersViewModel)
            }
        })
    }

    override fun fetchingOffersStarted() {
        binding.pbOffers.show()
    }

    override fun fetchingOffersSuccess(offersNumber:Int) {
        if(offersNumber==0) binding.tvOffers.show()
        binding.pbOffers.hide()
    }

    override fun fetchingOffersFailure(message: String) {
        binding.pbOffers.hide()
    }

    override fun userChoosedOfferStarted() {
        pbUserChoosedOffer.show()
    }

    override fun userChoosedOfferSuccess() {
        pbUserChoosedOffer.hide()
        /*Go to myAccountFragment to see your actual rides; On Stack is only the homeFragment*/
        navController.popBackStack(R.id.offersFragment, true)
        navController.navigate(R.id.myAccountFragment)
    }

    override fun userChoosedOfferFailure(message: String?) {
        pbUserChoosedOffer.hide()
        showToast(message.toString())
    }
}