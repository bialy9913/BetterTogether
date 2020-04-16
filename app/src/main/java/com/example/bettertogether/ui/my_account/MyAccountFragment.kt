package com.example.bettertogether.ui.my_account


import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.NavHostFragment
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

    override fun navigateToAuth() {
        navController.navigateWithoutComingBack(R.id.auth_nav_graph)
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
