package com.example.bettertogether.utils

import android.content.Context
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import java.util.regex.Pattern

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun NavController.navigateWithoutComingBack(@IdRes resIdDestination: Int) {
    val navOptions = NavOptions.Builder()
        .setPopUpTo(this.graph.id, false)
        .build()

    this.navigate(resIdDestination, null, navOptions)

}

fun ProgressBar.show(){
    this.visibility= View.VISIBLE
}

fun ProgressBar.hide(){
    this.visibility=View.GONE
}

fun ConstraintLayout.show(){
    this.visibility=View.VISIBLE
}

fun ConstraintLayout.hide(){
    this.visibility=View.GONE
}
