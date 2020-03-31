package com.example.bettertogether

import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bettertogether.utils.NetworkReceiver
import com.example.bettertogether.utils.navigateWithoutComingBack
import com.example.bettertogether.utils.showToast
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private val mainViewModel:MainViewModel by viewModel()
    private val navController by lazy { findNavController(R.id.navHostFragment) }
    private val networkReceiver= NetworkReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController.navigateWithoutComingBack(R.id.main_nav_graph,R.id.auth_nav_graph)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.parent?.id == R.id.auth_nav_graph) {
                bottomNav.visibility = View.GONE
                logOut.visibility = View.GONE
            }
            else {
                bottomNav.visibility = View.VISIBLE
                logOut.visibility=View.VISIBLE
            }
        }
        bottomNav.setupWithNavController(navController)
        registerReceiver(networkReceiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
        setOnClickListeners()
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)
    }
    private fun setOnClickListeners(){
        logOut.setOnClickListener {
            mainViewModel.logOut()
            navController.navigateWithoutComingBack(R.id.main_nav_graph,R.id.auth_nav_graph)
        }
    }
}