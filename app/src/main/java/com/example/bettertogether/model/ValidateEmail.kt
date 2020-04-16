package com.example.bettertogether.model

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