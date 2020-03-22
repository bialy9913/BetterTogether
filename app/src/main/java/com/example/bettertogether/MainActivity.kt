package com.example.bettertogether

import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bettertogether.utils.NetworkReceiver
import com.example.bettertogether.utils.navigateWithoutComingBack
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val navController by lazy { findNavController(R.id.navHostFragment) }
    private val networkReceiver= NetworkReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController.navigateWithoutComingBack(R.id.main_nav_graph,R.id.auth_nav_graph)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.parent?.id == R.id.auth_nav_graph)
                bottomNav.visibility = View.GONE
            else
                bottomNav.visibility = View.VISIBLE
        }
        bottomNav.setupWithNavController(navController)
        registerReceiver(networkReceiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
    }
    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logOut -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }*/
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)
    }
}