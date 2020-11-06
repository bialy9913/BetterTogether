package com.example.bettertogether.models

data class Offer(
    var offerUID:String?
    ,var currOfferUID:String?
    ,var UID:String?
    ,var userName:String?
    ,var userRating:String?
    ,var startPoint:String?
    ,var endPoint:String?
    ,var ridePrice:String?
    ,var rideDate: String?
    ,var additionalComment:String?
    ,var seatNumber: String?
    ,var passengerList: List<String>?
)