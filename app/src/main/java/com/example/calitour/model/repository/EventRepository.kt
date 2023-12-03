package com.example.calitour.model.repository

import android.net.Uri
import android.util.Log
import com.example.calitour.model.DTO.BadgeDTO
import com.example.calitour.model.DTO.EventDocumentDTO
import com.example.calitour.model.DTO.EventFullDTO
import com.example.calitour.model.entity.EntityFirestore
import com.example.calitour.model.DTO.PriceDTO
import com.google.firebase.firestore.ktx.firestore
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
        Log.e("<<<" , "eventos hallados $allEvents")
        val events = ArrayList<EventFullDTO>()
        events.addAll(allEvents)
        Log.e("<<<", "eventos convertidos  $events")
        events.forEach {
            val entityName = getEntityNameById(it.entityId)
            var photoUrl = it.img
            if(photoUrl.isNotEmpty()){
                photoUrl = getEventImage(it.entityId, it.id,it.img)!!
            }
            val price = getPricesEvent(it.id)[0].fee
            it.img = photoUrl
            it.entityName = entityName!!
            it.price = price.toInt()
        }
        return events
    }

    suspend fun reactToEvent(eventId: String, operation: String){
        val result = Firebase.firestore.collection("events")
            .document(eventId)
            .get()
            .await()

        Log.e("<<<", "encontrado ${result.toString()}")
        val event = result.toObject(EventDocumentDTO::class.java)
        var actual = event?.reaction as Int

        when (operation){
            "add" -> {
                actual++
            }
            "remove" -> {
                actual--
            }
        }

        Firebase.firestore.collection("events")
            .document(eventId)
            .update("reaction", actual)
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
        }
        return uriArrayList
    }

    suspend fun getEventBadges(id: String): ArrayList<BadgeDTO>{
        val event = getEventById(id)[0]
        val badges = Firebase.firestore.collection("events").document(event.id).collection("badges").get().await().documents
        var badgesArrayList = ArrayList<BadgeDTO>();
        badges.forEach{
            val badge = it.toObject(BadgeDTO::class.java)
            badge?.let {
                badgesArrayList.add(badge)
            }
        }
        return badgesArrayList
    }

    suspend fun getPricesEvent(id:String): ArrayList<PriceDTO>{
        val event = getEventById(id)[0]
        val prices = Firebase.firestore.collection("events").document(event.id).collection("prices").get().await().documents
        var pricesArrayList = ArrayList<PriceDTO>();
        prices.forEach{
            val price = it.toObject(PriceDTO::class.java)
            price?.let {
                pricesArrayList.add(price)
            }
        }
        return pricesArrayList
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