package com.example.bettertogether.models

data class SignUpCredentials (
    var email:String,
    var password:String,
    var userName:String,
    var defaultStartPoint:String?,
    var defaultStartPointLat:Double,
    var defaultStartPointLng:Double
)