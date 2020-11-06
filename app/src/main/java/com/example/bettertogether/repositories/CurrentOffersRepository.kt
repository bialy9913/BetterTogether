package com.example.bettertogether.repositories

import com.example.bettertogether.models.CurrOffer
import com.example.bettertogether.models.Offer
import com.example.bettertogether.utils.info
import com.example.bettertogether.utils.removePassengerFromList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CurrentOffersRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository
) {

    suspend fun addOffer(currOffer: CurrOffer, onStarted: () -> Unit, onSuccess:() -> Unit, onFailure:(String?) -> Unit){
        onStarted()
        val dataToAdd = hashMapOf(
            "offerUID" to currOffer.offerUID
            ,"UID" to currOffer.UID
            ,"startPoint" to currOffer.startPoint
            ,"endPoint" to currOffer.endPoint
            ,"ridePrice" to currOffer.ridePrice
            ,"rideDate" to currOffer.rideDate
            ,"additionalComment" to currOffer.additionalComment
            ,"driverUID" to currOffer.driverUID
            ,"driverUserName" to currOffer.driverUserName
            ,"driverRating" to currOffer.driverRating
            ,"seatNumber" to currOffer.seatNumber
            ,"passengerList" to currOffer.passengerList
            ,"type" to currOffer.type
            ,"status" to currOffer.status
        )
        firestore
            .collection("currOffers")
            .add(dataToAdd)
            .addOnSuccessListener {
                currOffer.currOfferUID = it.id
                info("Success in CurrentOffersRepository")
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it.message)
            }.await()
    }
    suspend fun getCurrOffers(onStarted: () -> Unit,onSuccess: (currOffersNumber:Int) -> Unit,onFailure: (String?) -> Unit):List<CurrOffer>{
        onStarted()
        val currOffers = ArrayList<CurrOffer>()
        firestore
            .collection("currOffers")
            .whereEqualTo("UID",firebaseAuth.currentUser!!.uid)
            .whereEqualTo("status","IN PROGRESS")
            .get()
            .addOnSuccessListener {documents ->
                onSuccess(documents.size())
                documents.forEach { document ->
                    currOffers.add(
                        CurrOffer(
                            document.id
                            ,document.get("offerUID").toString()
                            ,document.get("UID").toString()
                            ,document.get("startPoint").toString()
                            ,document.get("endPoint").toString()
                            ,document.get("ridePrice").toString()
                            ,document.get("rideDate").toString()
                            ,document.get("additionalComment").toString()
                            ,document.get("driverUID").toString()
                            ,document.get("driverUserName").toString()
                            ,document.get("driverRating").toString()
                            ,document.get("seatNumber").toString()
                            ,document.get("passengerList") as List<String>
                            ,document.get("type").toString()
                            ,document.get("status").toString()
                            )
                    )
                }
            }
            .addOnFailureListener {exception ->
                onFailure(exception.message)
            }
            .await()
        return currOffers.toList()
    }
    suspend fun getHistoryOffers(onStarted: () -> Unit,onSuccess: (historyOffersNumber:Int) -> Unit,onFailure: (String?) -> Unit):List<CurrOffer>{
        onStarted()
        val currOffers = ArrayList<CurrOffer>()
        firestore
            .collection("currOffers")
            .whereEqualTo("UID",firebaseAuth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {documents ->
                onSuccess(documents.size())
                documents.forEach { document ->
                    if(document.get("status").toString()!=("IN PROGRESS"))
                    currOffers.add(
                        CurrOffer(
                            document.id
                            ,document.get("offerUID").toString()
                            ,document.get("UID").toString()
                            ,document.get("startPoint").toString()
                            ,document.get("endPoint").toString()
                            ,document.get("ridePrice").toString()
                            ,document.get("rideDate").toString()
                            ,document.get("additionalComment").toString()
                            ,document.get("driverUID").toString()
                            ,document.get("driverUserName").toString()
                            ,document.get("driverRating").toString()
                            ,document.get("seatNumber").toString()
                            ,document.get("passengerList") as List<String>
                            ,document.get("type").toString()
                            ,document.get("status").toString()
                        )
                    )
                }
            }
            .addOnFailureListener {exception ->
                onFailure(exception.message)
            }
            .await()
        return currOffers.toList()
    }
    suspend fun cancelRide(currOffer: CurrOffer,onStarted: () -> Unit,onSuccess: () -> Unit,onFailure: (String?) -> Unit){
        onStarted()
        val currOffers = ArrayList<String>()
        if (currOffer.type.equals("PASSENGER")){
            firestore
                .collection("offers")
                .document(currOffer.offerUID!!)
                .get()
                .addOnSuccessListener {document ->
                    GlobalScope.launch { updateCurrOfferPassenger(currOffer,document.get("currOfferUID").toString(),currOffer.offerUID!!,document.get("passengerList") as List<String>,onSuccess,onFailure) }
                }
                .addOnFailureListener {
                    onFailure(it.message)
                }
                .await()
        }
        else{
            firestore
                .collection("currOffers")
                .whereEqualTo("offerUID",currOffer.offerUID)
                .get()
                .addOnSuccessListener {document ->
                    document.forEach{
                        currOffers.add(it.id)
                    }
                    GlobalScope.launch { updateCurrOffer(currOffers,currOffer.offerUID!!,onSuccess,onFailure) }
                }
                .addOnFailureListener {
                    onFailure(it.message)
                }
                .await()
        }
    }
    private suspend fun updateCurrOfferPassenger(passengerCurrOffer:CurrOffer,driverCurrOfferUID:String,offerUID:String,offerPassengerList:List<String>,onSuccess: () -> Unit, onFailure: (String?) -> Unit){
        val batch = firestore.batch()
        batch.update(firestore.collection("currOffers").document(passengerCurrOffer.currOfferUID!!),"offerUID","")
        batch.update(firestore.collection("currOffers").document(passengerCurrOffer.currOfferUID!!),"status","CANCEL")
        batch.update(firestore.collection("currOffers").document(driverCurrOfferUID),"passengerList",removePassengerFromList(offerPassengerList,userRepository.getUserName()))
        batch.update(firestore.collection("offers").document(offerUID),"passengerList",removePassengerFromList(offerPassengerList,userRepository.getUserName()))
        batch
            .commit()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it.message)
            }
            .await()
    }
    private suspend fun updateCurrOffer(currOffers:ArrayList<String>,offerUID:String,onSuccess: () -> Unit, onFailure: (String?) -> Unit){
        val batch = firestore.batch()
        val list = ArrayList<String>().toList()
        currOffers.forEach {
            batch.update(firestore.collection("currOffers").document(it),"offerUID","")
            batch.update(firestore.collection("currOffers").document(it),"passengerList",list)
            batch.update(firestore.collection("currOffers").document(it),"status","CANCEL")
        }
        batch.delete(firestore.collection("offers").document(offerUID))
        batch
            .commit()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure("Error occured during updating currOffers $it.message")
            }
            .await()
    }
}