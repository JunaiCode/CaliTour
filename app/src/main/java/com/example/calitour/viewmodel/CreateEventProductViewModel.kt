package com.example.calitour.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calitour.model.DTO.BadgeDTO
import com.example.calitour.model.DTO.EventDocumentDTO
import com.example.calitour.model.DTO.PriceDTO
import com.example.calitour.model.entity.Event
import com.example.calitour.model.repository.EventRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.UUID

class CreateEventProductViewModel: ViewModel() {

    var editEvent= MutableLiveData<EventDocumentDTO?>()

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
        }
    }

    fun getEventById(id:String): MutableLiveData<EventDocumentDTO?> {
        viewModelScope.launch (Dispatchers.IO){
            editEvent.postValue(eventrepo.getEventById(id)[0])
        }
        return editEvent
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
        viewModelScope.launch(Dispatchers.IO){
            //Cargar las insignias y imagen de eventos
            try{
                val badgeImg = UUID.randomUUID().toString()
                Firebase.storage.reference
                    .child("badges")
                    .child(e.entityId)
                    .child(e.id.toString())
                    .child(badgeImg)
                    .putFile(e.badges[0].img).await()

                Firebase.firestore.collection("events")
                    .document(e.id.toString())
                    .collection("badges")
                    .document(e.badges[0].id.toString())
                    .update("img", badgeImg)
                    .await()

                val eventImg = UUID.randomUUID().toString()
                Firebase.storage.reference
                    .child("eventImages")
                    .child(e.entityId)
                    .child(e.id.toString())
                    .child(eventImg)
                    .putFile(e.img).await()

                Firebase.firestore.collection("events")
                    .document(e.id.toString())
                    .update("img", eventImg)
                    .await()

            }catch (ex:Exception){
                Log.e(">>>>",ex.message.toString())
            }
        }
    }
}