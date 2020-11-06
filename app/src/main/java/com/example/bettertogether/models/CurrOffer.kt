package com.example.bettertogether.models


data class CurrOffer(
    var currOfferUID:String?
    ,var offerUID:String?
    ,var UID:String?
    ,var startPoint:String?
    ,var endPoint:String?
    ,var ridePrice:String?
    ,var rideDate: String?
    ,var additionalComment:String?
    ,var driverUID:String?
    ,var driverUserName: String?
    ,var driverRating:String?
    ,var seatNumber: String?
    ,var passengerList: List<String>?
    ,var type:String?
    ,var status:String?
)