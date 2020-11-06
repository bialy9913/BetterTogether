package com.example.bettertogether.ui.my_account


import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bettertogether.R
import com.example.bettertogether.ui.base.BaseFragment
import com.example.bettertogether.utils.navigateWithoutComingBack
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.fragment_my_account.*

class MyAccountFragment : BaseFragment(),MyAccountNavigator {

    private val myAccountViewModel:MyAccountViewModel by viewModel()
    private val navController by lazy { NavHostFragment.findNavController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAccountViewModel.setNavigator(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        myAccountViewModel.getCurrOffers()
        myAccountViewModel.offers.observe(viewLifecycleOwner, Observer { offers ->
            rvCurrOffers.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)
                it.adapter =
                    CurrOffersAdapter(offers, myAccountViewModel)
            }
        })
    }

    override fun navigateToAuth() {
        navController.navigateWithoutComingBack(R.id.auth_nav_graph)
    }

    override fun fetchingCurrOffersStarted() {
    }

    override fun fetchingCurrOffersSuccess(currOfferNumber: Int) {
    }

    override fun fetchingCurrOffersFailure(message: String?) {
        showToast(message!!)
    }

    override fun cancelRideStarted() {
    }

    override fun cancelRideSuccess() {
        this.parentFragmentManager.beginTransaction().detach(this).attach(this).commit()
    }

    override fun cancelRideFailure(message: String?) {
        showToast(message!!)
    }

    private fun setOnClickListeners(){
        toolbar.setOnMenuItemClickListener{
            if(it.itemId==R.id.signOutItem){
                myAccountViewModel.signOut()
            }
            if(it.itemId==R.id.settingsItem){
                navController.navigate(R.id.action_myAccountFragment_to_settingsFragment)
            }
            true
        }
    }
}
