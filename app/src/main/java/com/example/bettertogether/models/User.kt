package com.example.bettertogether.models

data class User(
    var name:String?,
    var maxDestinationDistance:String,
    var defaultStartPoint:String?,
    var defaultStartPointLat:Double,
    var defaultStartPointLng:Double
)