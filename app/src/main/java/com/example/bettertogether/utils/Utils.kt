package com.example.bettertogether.utils

import android.util.Patterns
import java.util.regex.Pattern


fun validateEmail(email:String):Boolean{
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun validatePassword(password:String):Boolean{
    return Pattern.compile(".{6,}").matcher(password).matches()
}

fun addPassengerToList(list: List<String>, passenger:String):List<String>{
    val array:ArrayList<String> = ArrayList()
    array.addAll(list)
    array.add(passenger)
    return array.toList()
}
fun removePassengerFromList(list:List<String>, passenger: String):List<String>{
    val array = ArrayList<String>()
    array.addAll(list)
    array.remove(passenger)
    return array.toList()
}
fun displayPassengers(list:List<String>):String{
    return list.joinToString("&")
}
fun convertToStringDate(day:Int,month:Int,year:Int):String{
    val tDay:String = if(day/10==0) "0$day" else day.toString()
    val tMonth:String = if(month/10==0) "0$month" else month.toString()
    return "$tDay-$tMonth-$year"
}