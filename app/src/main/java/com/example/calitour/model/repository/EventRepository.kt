package com.example.calitour.model.repository

import android.util.Log
import com.example.calitour.model.DTO.EventDocumentDTO
import com.example.calitour.model.entity.Event
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

class EventRepository {
    suspend fun getAllEvents():ArrayList<EventDocumentDTO>{
        val result = Firebase.firestore.collection("events").get().await()
        val events = result.documents
        var eventArraylist = ArrayList<EventDocumentDTO>();
        events.forEach{
            val event = it.toObject(EventDocumentDTO::class.java)
            event?.let {
                eventArraylist.add(event)
            }
        }
        return eventArraylist
    }

    suspend  fun getEventByEntityId(id:String): ArrayList<EventDocumentDTO>{
        val result = Firebase.firestore.collection("events")
            .whereEqualTo("entityId",id)
            .get()
            .await()
        val events = result.documents
        var eventArraylist = ArrayList<EventDocumentDTO>();
        events.forEach{
            val event = it.toObject(EventDocumentDTO::class.java)
            event?.let {
                eventArraylist.add(event)
            }
        }
        return eventArraylist
    }

    suspend  fun getEventsAvailablesByEntityId(id:String): ArrayList<EventDocumentDTO>{
        val result = Firebase.firestore.collection("events")
            .whereEqualTo("entityId",id)
            .whereEqualTo("state","available")
            .get()
            .await()
        val events = result.documents
        var eventArraylist = ArrayList<EventDocumentDTO>();
        events.forEach{
            val event = it.toObject(EventDocumentDTO::class.java)
            event?.let {
                eventArraylist.add(event)
            }
        }
        return eventArraylist
    }

    suspend  fun getEventsUnavailablesByEntityId(id:String): ArrayList<EventDocumentDTO>{
        val result = Firebase.firestore.collection("events")
            .whereEqualTo("entityId",id)
            .whereEqualTo("state","unavailable")
            .get()
            .await()
        val events = result.documents
        var eventArraylist = ArrayList<EventDocumentDTO>();
        events.forEach{
            val event = it.toObject(EventDocumentDTO::class.java)
            event?.let {
                eventArraylist.add(event)
            }
        }
        return eventArraylist
    }
}