package com.example.calitour.model.repository

import android.util.Log
import com.example.calitour.model.DTO.EventDocumentDTO
import com.example.calitour.model.DTO.EventFullDTO
import com.example.calitour.model.entity.EntityFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await


class EventRepository {
    suspend fun getAllEvents():ArrayList<EventDocumentDTO>{
        val result = Firebase.firestore.collection("events").get().await()
        val events = result.documents
        val eventArraylist = ArrayList<EventDocumentDTO>();
        events.forEach{
            val event = it.toObject(EventDocumentDTO::class.java)
            event?.let {
                eventArraylist.add(event)
            }
        }
        return eventArraylist
    }

    suspend fun getEntityNameById(entityId: String): String?{
        val result = Firebase.firestore
            .collection("entities")
            .document(entityId)
            .get()
            .await().toObject(EntityFirestore::class.java)
        return  result?.name
    }

    suspend fun getEventImage(entityId: String, eventId: String, photoId:String): String?{

        return Firebase.storage.reference
                    .child("eventImages")
                    .child(entityId)
                    .child(eventId)
                    .child(photoId)
                    .downloadUrl
                    .await()
                    .toString()
    }

    suspend fun getAllActiveEvents():ArrayList<EventFullDTO>{

        val result = Firebase.firestore.collection("events")
            .whereEqualTo("state", "available")
            .get()
            .await()

        val allEvents = result.toObjects(EventFullDTO::class.java)
        val events = ArrayList<EventFullDTO>()
        events.addAll(allEvents)
        events.forEach {
            val entityName = getEntityNameById(it.entityId)
            var photoUrl = it.img
            if(photoUrl.isNotEmpty()){
                photoUrl = getEventImage(it.entityId, it.id,it.img)!!
            }
            it.img = photoUrl
            it.entityName = entityName!!
        }
        return events
    }

    suspend fun reactToEvent(eventId: String){
        val result = Firebase.firestore.collection("events")
            .document(eventId)
            .get().addOnSuccessListener {
                val event = it.toObject(EventDocumentDTO::class.java)
                Log.e("<<<", "$eventId es ${event.toString()}")
                var actual = event?.reaction as Int
            }
            .await()

    }

    suspend fun getEventById(id:String): ArrayList<EventDocumentDTO> {
        val result = Firebase.firestore.collection("events")
            .whereEqualTo("id",id)
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