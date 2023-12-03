package com.example.calitour.model.repository

import android.net.Uri
import android.util.Log
import com.example.calitour.model.DTO.BadgeDTO
import com.example.calitour.model.DTO.EventDocumentDTO
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await


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

    suspend fun getEventImg(id:String): Uri{
        val event = getEventById(id)[0]
        val storageRef = Firebase.storage.reference
        val pathReference = storageRef.child("eventImages/${event.entityId}/${event.id}/${event.img}")
        return pathReference.downloadUrl.await()
    }

    suspend fun getEventBadgesImg(id:String): ArrayList<Uri>{
        val event = getEventById(id)[0]
        val badges = Firebase.firestore.collection("events").document(event.id).collection("badges").get().await().documents
        var badgesArrayList = ArrayList<BadgeDTO>();
        badges.forEach{
            val badge = it.toObject(BadgeDTO::class.java)
            badge?.let {
                badgesArrayList.add(badge)
            }
        }
        val storageRef = Firebase.storage.reference
        val uriArrayList = ArrayList<Uri>()
        badgesArrayList.forEach {
            val pathReference = storageRef.child("badges/${event.entityId}/${event.id}/${it.img}")
            uriArrayList.add(pathReference.downloadUrl.await())
            Log.e("A",uriArrayList.toString())
        }
        return uriArrayList
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