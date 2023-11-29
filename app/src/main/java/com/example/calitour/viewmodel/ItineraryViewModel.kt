package com.example.calitour.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calitour.components.adapter.ItineraryEventAdapter
import com.example.calitour.model.DTO.EventDocumentDTO
import com.example.calitour.model.entity.Badge
import com.example.calitour.model.entity.Event
import com.example.calitour.model.entity.ItineraryDay
import com.example.calitour.model.entity.ItineraryEvent
import com.example.calitour.model.entity.Price
import com.example.calitour.model.entity.Trivia
import com.example.calitour.model.repository.ItineraryRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ItineraryViewModel() : ViewModel() {

    // En el ViewModel
    private val _adapterEvents = MutableLiveData<List<ItineraryEvent>>()
    private val _itineraryEvents = MutableLiveData<List<ItineraryEvent>>()
    val adapterEvents: LiveData<List<ItineraryEvent>> get() = _adapterEvents

    val itineraryEvents: LiveData<List<ItineraryEvent>> get() = _itineraryEvents

    // Mover el adaptador al nivel de la clase
    private val adapter = ItineraryEventAdapter()

    fun getItineraryEventAdapter(): ItineraryEventAdapter {
        return adapter
    }



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
                loadEventDetails(itineraryDay.events)
                val formattedDate = itineraryDay.getFormattedDate()
                Log.e(">>>", "Formatted Date: $formattedDate")
            }
        }
    }
    fun loadEventDetails(eventsIdList: ArrayList<String>) {
        for (eventId in eventsIdList) {
            println("Event ID: $eventId")

            Firebase.firestore.collection("events")
                .document(eventId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    // Aquí manejas el resultado exitoso
                    if (documentSnapshot.exists()) {
                        // El documento existe, puedes trabajar con sus datos
                        val jsonDataEvent = Gson().toJson(documentSnapshot.data)
                        val eventDocumentDTO = Gson().fromJson(jsonDataEvent, EventDocumentDTO::class.java)

                        Log.e("JSON EVENT", jsonDataEvent)
                        Log.e("JSON DTO EVENT", eventDocumentDTO.toString())
                        //val prices = loadEventPrices(eventId)
                        //val badges = loadEventBadges(eventId)
                        //val trivias = loadEvenTrivias(eventId)

                        // Obtener la subcolección "prices"
                        val pricesCollection = documentSnapshot.reference.collection("prices")

                        // Realizar consulta para obtener el precio con "name" igual a "General"
                        pricesCollection.whereEqualTo("name", "General")
                            .get()
                            .addOnSuccessListener { pricesQuerySnapshot ->
                                for (priceDocument in pricesQuerySnapshot.documents) {
                                    // Aquí manejas los documentos de la subcolección "prices"
                                    val fee = priceDocument.getDouble("fee")

                                    //mostrar info del evento
                                    val event = eventDocumentDTO
                                    if (event != null) {
                                        // Ahora puedes acceder a las propiedades del objeto Event
                                        Log.e("Name:", event.name)
                                        Log.e("Place:", event.place)
                                        var eventTime = convertTimestampToFormattedTime(event.date)
                                        Log.e("Event Time:", eventTime)
                                        Log.e("Img:", event.img)
                                    }
                                    Log.e("Price:", fee.toString())
                                    //Log.e("PRICE FEE", "Fee for General: $fee")

                                    //Creo el objeto ItineraryEvent
                                    val itineraryEvent = fee?.let {
                                        ItineraryEvent(
                                            name = event.name,
                                            place = event.place,
                                            eventTime = convertTimestampToFormattedTime(event.date),
                                            img = event.img,
                                            price = it
                                        )
                                    }

                                    Log.e("Objeto:", itineraryEvent.toString())
                                    if (itineraryEvent != null) {
                                        adapter.addEvent(itineraryEvent)

                                    }
                                    if (adapter.itemCount == 0) {
                                        Log.d("ItineraryViewModel", "No hay eventos")
                                        //viewModel.searchEventsItineraryDay("hola")
                                        //showFragment(itineraryEmpty)
                                    } else {
                                        Log.d("ItineraryViewModel", "Hay eventos")
                                        //showFragment(itineraryFull)
                                    }

                                }
                            }
                            .addOnFailureListener { e ->
                                // Manejar errores al obtener datos de la subcolección "prices"
                                Log.e("loadEventDetails", "Error al obtener datos de la subcolección prices", e)
                            }

                    } else {
                        // El documento no existe
                        Log.e("loadEventDetails", "No existe un evento con ID: $eventId")
                    }
                }
                .addOnFailureListener { e ->
                    // Aquí manejas cualquier error al obtener datos del documento principal
                    Log.e("loadEventDetails", "Error al obtener datos para el evento con ID: $eventId", e)
                }
        }
    }
    fun convertTimestampToFormattedTime(timestamp: Timestamp): String {
        val date = Date(timestamp.seconds * 1000) // Convertir segundos a milisegundos
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(date)
    }

}


