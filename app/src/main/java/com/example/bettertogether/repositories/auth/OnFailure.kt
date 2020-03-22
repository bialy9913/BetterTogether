package com.example.bettertogether.repositories.auth

interface OnFailure {
    fun onFailure(message: String)
}