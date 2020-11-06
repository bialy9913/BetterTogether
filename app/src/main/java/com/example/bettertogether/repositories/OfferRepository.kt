package com.example.bettertogether.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.bettertogether.models.CurrOffer
import com.example.bettertogether.models.Offer
import com.example.bettertogether.utils.addPassengerToList
import com.example.bettertogether.utils.info
import com.example.bettertogether.utils.removePassengerFromList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OfferRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository
) {

    private suspend fun updateCurrOffer(currOffer: CurrOffer,offerUID:String,onSuccess: () -> Unit,onFailure: (String?) -> Unit){
        val documentReference = firestore.collection("currOffers").document(currOffer.currOfferUID!!)
        documentReference
            .update("offerUID",offerUID)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it.message)
            }.await()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addOffer(offer: Offer,currOffer: CurrOffer, onStarted:() -> Unit, onSuccess: () -> Unit, onFailure: (String?) -> Unit){
        onStarted()
        offer.UID = firebaseAuth.currentUser!!.uid
        offer.userName = userRepository.getUserName()
        info("Pobranie nazwy uzytkownika")
        val dataToAdd = hashMapOf("currOfferUID" to currOffer.currOfferUID
                                                        ,"UID" to offer.UID
                                                        ,"userName" to offer.userName
                                                        ,"userRating" to offer.userRating
                                                        ,"startPoint" to offer.startPoint
                                                        ,"endPoint" to offer.endPoint
                                                        ,"ridePrice" to offer.ridePrice
                                                        ,"rideDate" to currOffer.rideDate
                                                        ,"additionalComment" to offer.additionalComment
                                                        ,"seatNumber" to offer.seatNumber
                                                        ,"passengerList" to offer.passengerList
                                                      )
        firestore
            .collection("offers")
            .add(dataToAdd)
            .addOnSuccessListener {
                info("Success in OfferRepository")
                GlobalScope.launch {
                    updateCurrOffer(currOffer,it.id,onSuccess,onFailure)
                }
            }
            .addOnFailureListener {
                onFailure(it.message)
            }.await()
    }
    suspend fun addPassengerToList(offer:Offer,onSuccess: () -> Unit,onFailure: (String?) -> Unit){
        val documentReference = firestore.collection("offers").document(offer.offerUID!!)
        val documentReference1 = firestore.collection("currOffers").document(offer.currOfferUID!!)
        val batch = firestore.batch()
        batch.update(documentReference,"passengerList",
            addPassengerToList(offer.passengerList!!,userRepository.getUserName())
        )
        batch.update(documentReference1,"passengerList",
            addPassengerToList(offer.passengerList!!,userRepository.getUserName())
        )
        batch.commit()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    onSuccess()
                }
                else{
                    onFailure(it.exception!!.message)
                }
            }.await()
    }
    suspend fun getOffers(startPoint:String,endPoint:String,radius:String,onStarted:() -> Unit,onSuccess: (offersNumber:Int) -> Unit, onFailure: (String?) -> Unit):List<Offer>{
        onStarted()
        val offers = ArrayList<Offer>()
        firestore
            .collection("offers")
            .whereEqualTo("startPoint",startPoint)
            .whereEqualTo("endPoint",endPoint)
            .get()
            .addOnSuccessListener {documents ->
                onSuccess(documents.size())
                documents.forEach { document ->
                    if(document.get("UID")!=firebaseAuth.currentUser!!.uid && (document.get("passengerList") as List<String>).size<document.get("seatNumber").toString().toInt()){
                        offers.add(Offer(
                            document.id
                            ,document.get("currOfferUID").toString()
                            ,document.get("UID").toString()
                            ,document.get("userName").toString()
                            ,document.get("userRating").toString()
                            ,document.get("startPoint").toString()
                            ,document.get("endPoint").toString()
                            ,document.get("ridePrice").toString()
                            ,document.get("rideDate").toString()
                            ,document.get("additionalComment").toString()
                            ,document.get("seatNumber").toString()
                            ,document.get("passengerList") as List<String>
                            )
                        )
                    }
                }
            }
            .addOnFailureListener {exception ->
                onFailure(exception.message)
            }
            .await()
        return offers.toList()
    }
}