package com.example.bettertogether.model

import androidx.databinding.ObservableField

data class ValidatePassword(
    var password:String,
    var isValid:Boolean
){
    var error: ObservableField<String?> = ObservableField()

    fun setError(message:String?){
        error.set(message)
        error.notifyChange()
    }
}