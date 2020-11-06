package com.example.bettertogether.ui.history


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bettertogether.R
import com.example.bettertogether.ui.base.BaseFragment
import com.example.bettertogether.ui.my_account.CurrOffersAdapter
import kotlinx.android.synthetic.main.fragment_my_account.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.databinding.DataBindingUtil
import com.example.bettertogether.databinding.FragmentHistoryBinding
import com.example.bettertogether.utils.hide
import com.example.bettertogether.utils.show
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : BaseFragment(),HistoryNavigator {

    private val historyViewModel: HistoryViewModel by viewModel()
    private val navController by lazy { NavHostFragment.findNavController(this) }
    private lateinit var binding:FragmentHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        historyViewModel.setNavigator(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_history, container, false
        )
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        historyViewModel.getCurrOffers()
        historyViewModel.offers.observe(viewLifecycleOwner, Observer { offers ->
            rvHistoryOffers.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)
                it.adapter =
                    HistoryOffersAdapter(offers, historyViewModel)
            }
        })
    }

    override fun fetchingCurrOffersStarted() {
        binding.pbHistoryOffers.show()
    }

    override fun fetchingCurrOffersSuccess(historyOfferNumber: Int) {
        binding.pbHistoryOffers.hide()
        if(historyOfferNumber==0) binding.tvHistoryOffers.show()
    }

    override fun fetchingCurrOffersFailure(message: String?) {
        binding.pbHistoryOffers.hide()
    }
}
