package com.example.calitour.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calitour.model.entity.Event
import com.example.calitour.model.entity.ItineraryDay
import com.example.calitour.model.repository.ItineraryRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date

class ItineraryViewModel : ViewModel() {
    val _itineraryDay = MutableLiveData<ItineraryDay>()
    val repository = ItineraryRepository()

    private val _currentDate = MutableLiveData<Date>()
    val currentDate: LiveData<Date> get() = _currentDate

    init {
        // Observar cambios en currentDate
        currentDate.observeForever { date ->
            // Realizar la consulta cuando cambie currentDate
            searchEventsItineraryDay(date.toString())  // Puedes ajustar esto según tus necesidades
        }
    }

    fun setCurrentDate(date: Date) {
        _currentDate.value = date
    }

    fun searchEventsItineraryDay(day: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(">>>", "SE MODIFICO LA FECHA")
            Log.d(">>>", day)
            val result = Firebase.firestore.collection("users")
                .document("BGlarOVaQGXPgMG72rQMZ1oOLhx1")
                .collection("itinerary")
                .document("cHu0EhWU6omgBC81PtbN")
                .get().await()

            result?.let {
                val jsonData = Gson().toJson(it.data)
                Log.e(">>>", "Dia Actual:${day}")
                Log.e(">>>", it.id)
                Log.e(">>>", jsonData)
                val itineraryDay = Gson().fromJson(jsonData, ItineraryDay::class.java)
                Log.e(">>>", "Itinerary ID: ${itineraryDay.id}")
                Log.e(">>>", "Day Seconds: ${itineraryDay.day.seconds}")
                Log.e(">>>", "Events: ${itineraryDay.events}")
                val formattedDate = itineraryDay.getFormattedDate()
                Log.e(">>>", "Formatted Date: $formattedDate")
            }
        }
    }

    /*private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> get() = _events

    fun loadEventDetails() {
        val itineraryDay = _itineraryDay.value

        itineraryDay?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val eventDetails = mutableListOf<Event>()

                for (eventId in it.events) {
                    // Suponiendo que tienes un método en tu repositorio para obtener detalles de eventos por ID
                    val event = repository.getEventById(eventId)
                    event?.let {
                        eventDetails.add(it)
                    }
                }

                _events.postValue(eventDetails)
            }
        }
    }*/
}


