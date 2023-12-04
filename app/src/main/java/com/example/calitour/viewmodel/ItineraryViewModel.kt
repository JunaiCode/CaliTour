package com.example.calitour.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calitour.model.DTO.EventDocumentDTO
import com.example.calitour.model.DTO.EventItineraryDTO
import com.example.calitour.model.DTO.ItineraryDTO
import com.example.calitour.model.repository.EventRepository
import com.example.calitour.model.repository.ItineraryRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItineraryViewModel : ViewModel() {
    var eventRepo = EventRepository()
    var itineraryRepo = ItineraryRepository()
    var itineraryDay = MutableLiveData<ItineraryDTO>()
    var event = MutableLiveData<EventItineraryDTO>()
    var eventItineraryQuery = MutableLiveData<EventItineraryDTO>()


    var eventsItineraryQuery = MutableLiveData<ArrayList<EventItineraryDTO>>()
    val events: LiveData<ArrayList<EventItineraryDTO>> get() = eventsItineraryQuery

    fun getAllEventsItineraryDay(events: Array<String>) {

    }
    fun getEventById(eventId:String): MutableLiveData<EventItineraryDTO> {
        viewModelScope.launch (Dispatchers.IO){
            event.postValue(eventRepo.getEventItineraryById(eventId))
        }
        return event
    }

    fun getItineraryByDate(day: String): MutableLiveData<ItineraryDTO> {
        var userId = Firebase.auth.currentUser!!.uid
        viewModelScope.launch (Dispatchers.IO){
            val itineraryList = itineraryRepo.getItineraryByDate(userId, day)
            if (itineraryList.isNotEmpty()) {
                itineraryDay.postValue(itineraryList[0])
            } else {
                Log.w("ItineraryViewModel", "La lista de itinerarios está vacía para el día $day")
            }
        }
        return itineraryDay
    }

    fun getEventItineraryByDate(day: String): MutableLiveData<EventItineraryDTO> {
        var userId = Firebase.auth.currentUser!!.uid
        viewModelScope.launch (Dispatchers.IO){
            val itineraryList = itineraryRepo.getItineraryByDate(userId, day)
            if (itineraryList.isNotEmpty()) {
                var events = itineraryList[0].events
                for (event in events) {
                    eventItineraryQuery.postValue(eventRepo.getEventItineraryById(event))
                }
            } else {
                Log.w("ItineraryViewModel", "La lista de itinerarios está vacía para el día $day")
            }
        }
        return eventItineraryQuery
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getEventsItineraryByDate(day: String): MutableLiveData<ArrayList<EventItineraryDTO>> {
        var userId = Firebase.auth.currentUser!!.uid
        var eventsList= arrayListOf<EventItineraryDTO>()
        eventsItineraryQuery=MutableLiveData<ArrayList<EventItineraryDTO>>()
        viewModelScope.launch (Dispatchers.IO){
            val itineraryList = itineraryRepo.getItineraryByDate(userId, day)
            if (itineraryList.isNotEmpty()) {
                var events = itineraryList[0].events
                for (event in events) {
                    eventsList.add(eventRepo.getEventItineraryById(event))
                }
                eventsItineraryQuery.postValue(eventsList)
            } else {
                Log.w("ItineraryViewModel", "La lista de itinerarios está vacía para el día $day")
            }
        }
        return eventsItineraryQuery
    }
}
