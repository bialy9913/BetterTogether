package com.example.bettertogether.models

import androidx.databinding.ObservableField

data class ValidateEmail(
    var email:String,
    var isValid:Boolean
){

    var error:ObservableField<String?> = ObservableField()
    fun setError(message:String?){
        error.set(message)
        error.notifyChange()
    }
}