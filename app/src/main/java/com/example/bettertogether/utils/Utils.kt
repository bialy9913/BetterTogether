package com.example.bettertogether.utils

import android.util.Patterns
import java.util.regex.Pattern


fun validateEmail(email:String):Boolean{
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun validatePassword(password:String):Boolean{
    return Pattern.compile(".{6,}").matcher(password).matches()
}