package com.example.bettertogether.utils

import android.content.Context
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import java.util.regex.Pattern

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun NavController.navigateWithoutComingBack(@IdRes resIdSource: Int, @IdRes resIdDestination: Int) {
    val navOptions = NavOptions.Builder()
        .setPopUpTo(resIdSource, true)
        .build()

    this.navigate(resIdDestination, null, navOptions)

}

fun ProgressBar.show(){
    this.visibility= View.VISIBLE
}

fun ProgressBar.hide(){
    this.visibility=View.GONE
}

fun EditText.validateEmail(){
    this.error=
        if (!Patterns.EMAIL_ADDRESS.matcher(this.text.toString()).matches()){
            "Please enter valid e-mail address"
        }
        else
            null
}

fun EditText.validatePassword(){
    val pattern= Pattern.compile(".{6,}")
    this.error=
        if (!pattern.matcher(this.text.toString()).matches()){
            "Password doesn't match our policy"
        }
        else
            null
}
