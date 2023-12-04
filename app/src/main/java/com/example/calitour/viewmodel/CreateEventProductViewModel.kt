package com.example.calitour.viewmodel

import android.content.Intent
import android.net.Uri
import android.util.Dumpable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calitour.activities.ProfileEntityActivity
import com.example.calitour.activities.fragments.ActiveEventFragment
import com.example.calitour.model.DTO.BadgeDTO
import com.example.calitour.model.DTO.EventDocumentDTO
import com.example.calitour.model.DTO.PriceDTO
import com.example.calitour.model.entity.Event
import com.example.calitour.model.repository.EventRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.UUID

class CreateEventProductViewModel: ViewModel() {

    var editEvent= MutableLiveData<EventDocumentDTO?>()
    var eventImgUri = MutableLiveData<Uri>()
    var eventBadgesUri = MutableLiveData<ArrayList<Uri>>()
    var eventBadges = MutableLiveData<ArrayList<BadgeDTO>>()
    var eventPrices = MutableLiveData<ArrayList<PriceDTO>>()
    var uploadedComplete = MutableLiveData<ArrayList<Boolean>>()
    val eventrepo = EventRepository()

    //Formato en el que se debe ingresar la fecha y hora para que lo coja como timeStamp
    val dateFormat = SimpleDateFormat("dd-M-yyyy hh:mm:ss")

    fun createEvent(e: Event) {
        val eventDto = EventDocumentDTO(e.category,Timestamp(Date(e.date)),e.description,
            e.entityId,e.id.toString(),"",e.name,e.place,e.reaction,e.score,e.state)
        val priceDTO = PriceDTO(e.prices[0].description,e.prices[0].fee,e.prices[0].id.toString(),e.prices[0].name)
        val badgeDTO = BadgeDTO(e.badges[0].id.toString(),"",e.badges[0].name)
        viewModelScope.launch(Dispatchers.IO){
            Firebase.firestore.collection("events").document(eventDto.id).set(eventDto)
            Firebase.firestore.collection("events").document(eventDto.id).collection("prices").document(
                priceDTO.id).set(priceDTO)
            Firebase.firestore.collection("events").document(eventDto.id).collection("badges").document(
                badgeDTO.id).set(badgeDTO)
            uploadImages(e)
        }
    }

    fun editEvent(e:Event){
        val eventDto = EventDocumentDTO(e.category,Timestamp(Date(e.date)),e.description,
            e.entityId,e.id.toString(),getEventById(e.id.toString()).value?.img.toString(),e.name,e.place,e.reaction,e.score,e.state)
        val priceDTO = PriceDTO(e.prices[0].description,e.prices[0].fee,e.prices[0].id.toString(),e.prices[0].name)
        val badgeDTO = BadgeDTO(e.badges[0].id.toString(),getEventBadges(e.id.toString()).value?.get(0)?.img.toString(),e.badges[0].name)
        viewModelScope.launch(Dispatchers.IO){
            Firebase.firestore.collection("events").document(eventDto.id).set(eventDto)
            Firebase.firestore.collection("events").document(eventDto.id).collection("prices").document(
                priceDTO.id).set(priceDTO)
            Firebase.firestore.collection("events").document(eventDto.id).collection("badges").document(
                badgeDTO.id).set(badgeDTO)
            editImages(e)
        }
    }

     fun deleteEvent(e:EventDocumentDTO){
        viewModelScope.launch(Dispatchers.IO){
            Firebase.firestore.collection("events").document(e.id).update("state","deleted")
        }
    }

    fun getEventById(id:String): MutableLiveData<EventDocumentDTO?> {
        viewModelScope.launch (Dispatchers.IO){
            editEvent.postValue(eventrepo.getEventById(id)[0])
        }
        return editEvent
    }

    fun getPricesEvent(id:String): MutableLiveData<ArrayList<PriceDTO>>{
        viewModelScope.launch (Dispatchers.IO){
            eventPrices.postValue(eventrepo.getPricesEvent(id))
        }
        return eventPrices
    }

    fun getImgEvent(id:String):MutableLiveData<Uri>{
        viewModelScope.launch (Dispatchers.IO){
            eventImgUri.postValue(eventrepo.getEventImg(id))
        }
        return eventImgUri
    }

    fun getEventBadges(id: String): MutableLiveData<ArrayList<BadgeDTO>>{
        viewModelScope.launch (Dispatchers.IO){
            eventBadges.postValue(eventrepo.getEventBadges(id))
        }
        return eventBadges
    }

    fun getImgBadge(id:String):MutableLiveData<ArrayList<Uri>>{
        viewModelScope.launch (Dispatchers.IO){
            eventBadgesUri.postValue(eventrepo.getEventBadgesImg(id))
        }
        return eventBadgesUri
    }

    fun clearEditEvent(){
        editEvent.postValue(null)
    }

     fun dateToMilliseconds(date:String,dateFormat: SimpleDateFormat):Long{
        val mDate = dateFormat.parse(date)
        return mDate.time
    }

    fun millisecondsToDate(milliseconds:String,dateFormat: SimpleDateFormat):String{
        val millis: Long = milliseconds.toLong()
        return  dateFormat.format(millis)
    }

    fun uploadImages(e: Event){
        val badgeImg = UUID.randomUUID().toString()
        val eventImg = UUID.randomUUID().toString()
        val eventImgRef = Firebase.storage.reference.child("eventImages/${e.entityId}/${e.id}/${eventImg}")
        val badgeImgRef = Firebase.storage.reference.child("badges/${e.entityId}/${e.id}/${badgeImg}")
        val eventsRef = Firebase.firestore.collection("events").document("${e.id}")
        val badgesRef = Firebase.firestore.collection("events").document("${e.id}").collection("badges").document(e.badges[0].id.toString())
        eventImgRef.putFile(e.img).addOnCompleteListener(){ task->
            val temp = ArrayList<Boolean>(uploadedComplete.value)
            temp.add(true)
            uploadedComplete.postValue(temp)
            Log.e("Aja",uploadedComplete.value.toString())
        }
        badgeImgRef.putFile(e.badges[0].img).addOnCompleteListener(){ task->
            val temp = ArrayList<Boolean>(uploadedComplete.value)
            temp.add(true)
            uploadedComplete.postValue(temp)
            Log.e("Aja",uploadedComplete.value.toString())
        }
        badgesRef.update("img",badgeImg)
        eventsRef.update("img",eventImg)
    }

    fun editImages(e: Event){
        viewModelScope.launch(Dispatchers.IO){
            //Cargar las insignias y imagen de eventos
            val badgeImg = eventBadges.value?.get(0)?.img.toString()
            val eventImg = editEvent.value?.img.toString()
            try{
                val eventImgRef = Firebase.storage.reference.child("eventImages/${e.entityId}/${e.id}/${eventImg}")
                val badgeImgRef = Firebase.storage.reference.child("badges/${e.entityId}/${e.id}/${badgeImg}")
                badgeImgRef.putFile(e.badges[0].img)
                eventImgRef.putFile(e.img)
            }catch (ex:Exception){
                Log.e(">>>>",ex.message.toString())
            }
        }
    }
}