package com.example.calitour.model.repository

import com.example.calitour.model.entity.ItineraryDay
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ItineraryRepository {

    suspend fun getEventsItineraryDayById(id:String) : ItineraryDay?{
        val document= Firebase.firestore.collection("users").document("BGlarOVaQGXPgMG72rQMZ1oOLhx1").collection("itinerary").document("cHu0EhWU6omgBC81PtbN").get().await()
        val itineraryDay = document.toObject(ItineraryDay::class.java)
        return itineraryDay
    }
}
