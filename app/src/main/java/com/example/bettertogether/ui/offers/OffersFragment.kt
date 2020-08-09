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
import com.example.bettertogether.models.Offer
import com.example.bettertogether.models.OffersAdapter
import com.example.bettertogether.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_offers.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class OffersFragment: BaseFragment(),OffersNavigator {

    private val offersViewModel: OffersViewModel by viewModel()
    private lateinit var binding : FragmentOffersBinding
    private val navController by lazy { NavHostFragment.findNavController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        offersViewModel.setNavigator(this)
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
        offersViewModel.getMovies()
        offersViewModel.offers.observe(viewLifecycleOwner, Observer { offers ->
            recycler_view_offers.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)
                it.adapter = OffersAdapter(offers,this)
            }
        })
    }
    override fun userChoosedOffer(offer: Offer) {
        showToast("User choosed an offer: ${offer.UID} ${offer.userName}")
    }
}