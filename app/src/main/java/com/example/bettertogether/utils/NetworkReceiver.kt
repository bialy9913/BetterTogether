package com.example.bettertogether.utils

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.example.bettertogether.R
import kotlin.system.exitProcess

class NetworkReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        checkConnection(context)
    }

    private fun checkConnection(context: Context){
        val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = conn.activeNetworkInfo
        val isConnected = networkInfo != null && networkInfo.isConnected
        when{
            !isConnected -> {
                val builder = AlertDialog.Builder(context,
                    R.style.AlertDialogTheme
                )
                builder.setTitle("No internet Connection")
                builder.setMessage("Please turn on internet connection to continue")
                builder.setCancelable(false)
                builder.setPositiveButton("Refresh", DialogInterface.OnClickListener { dialog, which -> dialog.run { checkConnection(context) } })
                builder.setNegativeButton("Close app",
                    DialogInterface.OnClickListener { dialog, which -> dialog.run { exitProcess(-1) } })
                val alertDialog = builder.create()
                alertDialog.show()
            }
        }
    }
}