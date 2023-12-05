package com.example.calitour.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calitour.model.DTO.EventDTO
import com.example.calitour.model.DTO.ReviewDTO
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EventHistorialViewModel : ViewModel() {
    private val reviews : MutableCollection<ReviewDTO> = arrayListOf()
    private val eventsId : MutableCollection<String> = arrayListOf()
    private val events : MutableCollection<EventDTO> = arrayListOf()
    val historialData = MutableLiveData<MutableCollection<EventDTO>>()
    //TODO(bring comments, sort ids, search events by ids)

    private fun getReviews(){

            Firebase.firestore.collection("reviews").get()
                .addOnSuccessListener {
                    for (review in it.documents){

                        if ((review.get("idUser") as String).equals(Firebase.auth.currentUser!!.uid.toString())){
                            val temporalReview = ReviewDTO(
                                review.get("comment") as String,
                                review.get("id") as String,
                                review.get("idEvent") as String,
                                review.get("idUser") as String,
                                review.get("Score") as Long
                            )
                            reviews.add(temporalReview)
                        }

                    }
                }.addOnFailureListener{
                    Log.e(">>>", it.message.toString())
                }

    }

    private fun getEventsIds(){
        for (review in reviews){
            val currentEventId = review.idEvent
            if (!eventsId.contains(currentEventId)){
                eventsId.add(currentEventId)
            }
        }
    }

    private fun getEvent(id : String){
        viewModelScope.launch(Dispatchers.IO) {
            var newItem = Firebase.firestore.collection("events").document(id).get().await()
            var newEventDTO = newItem.toObject(EventDTO::class.java)
            withContext(Dispatchers.Main){
                events.add(newEventDTO!!)
            }
        }




    }

    private fun getEvents(){
        for (event in eventsId){
            getEvent(event)
        }
    }

    fun loadHistorial(){
        viewModelScope.launch (Dispatchers.IO) {
            getReviews()
            getEventsIds()
            getEvents()
            historialData.value = events
        }
    }
}