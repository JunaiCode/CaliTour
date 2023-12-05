package com.example.calitour.model.repository

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.calitour.model.DTO.BadgeDTO
import com.example.calitour.model.DTO.EventDocumentDTO
import com.example.calitour.model.DTO.EventFullDTO
import com.example.calitour.model.DTO.EventItineraryDTO
import com.example.calitour.model.DTO.EventTriviaDTO
import com.example.calitour.model.entity.EntityFirestore
import com.example.calitour.model.DTO.PriceDTO
import com.example.calitour.model.DTO.QuestionDTO
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


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

    suspend fun getAllEventsItinerary(userId:String, day: String):ArrayList<EventDocumentDTO>{
        var eventItineraryArraylist = ArrayList<EventDocumentDTO>();
        val result = Firebase.firestore.collection("users").document(userId).collection("itinerary")
            .whereEqualTo("day", day)
            .get().await()
        val allEventsItinerary = result.toObjects(EventItineraryDTO::class.java)
        val eventsItinerary = ArrayList<EventItineraryDTO>()
        eventsItinerary.addAll(allEventsItinerary)

        return eventItineraryArraylist
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
            val price = getPricesEvent(it.id)[0].fee
            it.img = photoUrl
            it.entityName = entityName!!
            it.price = price.toInt()
        }
        return events
    }

    suspend fun getAllEntityActiveEvents(entityId: String):ArrayList<EventFullDTO>{

        val result = Firebase.firestore.collection("events")
            .whereEqualTo("entityId", entityId)
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
            val price = getPricesEvent(it.id)[0].fee
            it.img = photoUrl
            it.entityName = entityName!!
            it.price = price.toInt()
        }
        return events
    }

    suspend fun getAllEntityPassedEvents(entityId: String):ArrayList<EventFullDTO>{

        val result = Firebase.firestore.collection("events")
            .whereEqualTo("entityId", entityId)
            .whereEqualTo("state", "unavailable")
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
            val price = getPricesEvent(it.id)[0].fee
            it.img = photoUrl
            it.entityName = entityName!!
            it.price = price.toInt()
        }
        return events
    }


    suspend fun getFilteredEvents(category: String): ArrayList<EventFullDTO> {
        val result = Firebase.firestore.collection("events")
            .whereEqualTo("category", category)
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

    suspend fun getEventByIdFull(eventId: String): EventFullDTO {
        val result = Firebase.firestore.collection("events")
            .document(eventId)
            .get()
            .await()
        val event = result.toObject(EventFullDTO::class.java)!!
        if(event.img != ""){
            event.img = getEventImage(event.entityId, event.id, event.img)!!

        }
        event.entityName = getEntityNameById(event.entityId)!!

        return event!!
    }

    suspend fun getAllPricesAvailableEventsByEntityId(id: String): ArrayList<String> {
        val events = getEventsAvailablesByEntityId(id)
        val prices = ArrayList<String>()
        events.forEach { event ->
            prices.add(getPricesEvent(event.id)[0].fee.toInt().toString())
        }
        return prices
    }

    suspend fun getAllPricesUnavailableEventsByEntityId(id: String): ArrayList<String> {
        val events = getEventsUnavailablesByEntityId(id)
        val prices = ArrayList<String>()
        events.forEach { event ->
            prices.add(getPricesEvent(event.id)[0].fee.toInt().toString())
        }
        return prices
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getEventItineraryById(eventId: String): EventItineraryDTO {
        val result = Firebase.firestore.collection("events")
            .document(eventId)
            .get()
            .await()
        val event = result.toObject(EventItineraryDTO::class.java)!!
        val timestamp = event.date
        val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp.seconds * 1000), ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formattedTime = localDateTime.format(formatter)
        event.eventTime=formattedTime
        if(event.img != ""){
            event.img = getEventImage(event.entityId, event.id, event.img)!!

        }
        return event!!

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

    suspend fun getEventsActiveImgByEntity(id:String): ArrayList<Uri>{
        val events = getEventsAvailablesByEntityId(id)
        var uris = ArrayList<Uri>();
        events.forEach{
             uris.add(getEventImg(it.id))
        }
        return uris
    }

    suspend fun getEventsInactiveImgByEntity(id:String): ArrayList<Uri>{
        val events = getEventsUnavailablesByEntityId(id)
        var uris = ArrayList<Uri>();
        events.forEach{
            uris.add(getEventImg(it.id))
        }
        return uris
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

    suspend  fun getEventsAvailablesTriviaByEntityId(id:String): ArrayList<EventTriviaDTO>{
        val result = Firebase.firestore.collection("events")
            .whereEqualTo("entityId",id)
            .whereEqualTo("state","available")
            .get()
            .await()
        val events = result.documents
        var eventArraylist = ArrayList<EventTriviaDTO>();
        events.forEach{
            val event = it.toObject(EventTriviaDTO::class.java)
            event?.let {
                eventArraylist.add(event)
            }
        }
        return eventArraylist
    }

    suspend fun getQuestionsByEventId(eventId: String): ArrayList<QuestionDTO>{
        val result = Firebase.firestore.collection("events")
            .document(eventId).collection("questions")
            .get()
            .await()
        val questions = result.documents
        var questionsArraylist = ArrayList<QuestionDTO>();
        questions.forEach{
            val question = it.toObject(QuestionDTO::class.java)
            question?.let {
                questionsArraylist.add(question)
            }
        }
        return questionsArraylist
    }

    suspend fun updateQuestion(eventId: String, questionId: String, updatedTitle: String, updatedOptions: List<String>) {
        val questionRef = Firebase.firestore.collection("events")
            .document(eventId)
            .collection("questions")
            .document(questionId)

        val updates = hashMapOf(
            "title" to updatedTitle,
            "correctAns" to updatedOptions.get(0),
            "options" to updatedOptions
        )

        questionRef.update(updates).await()
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