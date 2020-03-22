package com.example.bettertogether.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun NavController.navigateWithoutComingBack(@IdRes resIdSource: Int, @IdRes resIdDestination: Int) {
    val navOptions = NavOptions.Builder()
        .setPopUpTo(resIdSource, true)
        .build()

    this.navigate(resIdDestination, null, navOptions)

}
