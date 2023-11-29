package com.example.calitour.activities.fragments

import DatePickerFragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.calitour.R
import com.example.calitour.components.adapter.ItineraryEventAdapter
import com.example.calitour.databinding.ItineraryFragmentBinding
import com.example.calitour.model.DTO.EventDocumentDTO
import com.example.calitour.model.entity.ItineraryDay
import com.example.calitour.model.entity.ItineraryEvent
import com.example.calitour.viewmodel.ItineraryViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ItineraryFragment : Fragment() {
    lateinit var adapter: ItineraryEventAdapter
    private val itineraryFull = ItineraryFullFragment()
    private val itineraryEmpty = ItineraryEmptyFragment()

    private var currentDate: Date = Calendar.getInstance().time
    private lateinit var binding: ItineraryFragmentBinding

    private val viewModel: ItineraryViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItineraryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateDateTextView()
        adapter= ItineraryEventAdapter()


        /*if (adapter.itemCount == 0) {
            Log.d("ItineraryFragment", "No hay eventos")
            //showFragment(itineraryEmpty)
        } else {
            Log.d("ItineraryFragment", "Hay eventos")
            //showFragment(itineraryFull)
        }*/

        //showFragment(itineraryEmpty)

        binding.calendarPrevBtn.setOnClickListener {
            currentDate = getPreviousDay(currentDate)
            updateDateTextView()
        }

        binding.calendarNextBtn.setOnClickListener {
            currentDate = getNextDay(currentDate)
            updateDateTextView()
        }

        binding.calendarCardView.setOnClickListener {
            openDatePicker()
        }

    }

    private fun updateDateTextView() {
        if (::binding.isInitialized) {
            val dateFormat = SimpleDateFormat("EEE, MMM dd / yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(currentDate)
            binding.pageDateTV.text = formattedDate
            Log.d("currentDate", currentDate.toString())
           // viewModel.setCurrentDate(currentDate)
            searchEventsItineraryDay(currentDate.toString())
        }
    }

    private fun getPreviousDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        return calendar.time
    }

    private fun getNextDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        return calendar.time
    }

    fun showFragmentWithAdapter(fragment: Fragment, adapter: ItineraryEventAdapter) {
        if (fragment is ItineraryFullFragment) {
            fragment.setAdapter(adapter)
        }
        childFragmentManager.beginTransaction().replace(R.id.fragmentEventsItineraryContainer, fragment).commit()
    }

    fun showFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().replace(R.id.fragmentEventsItineraryContainer, fragment).commit()
    }


    private fun openDatePicker() {
        val datePickerFragment = DatePickerFragment()
        datePickerFragment.itineraryFragment = this

        val bundle = Bundle()
        bundle.putLong(DatePickerFragment.SELECTED_DATE_KEY, currentDate.time)
        datePickerFragment.arguments = bundle

        datePickerFragment.show(parentFragmentManager, DatePickerFragment.TAG)
    }

    fun updateDate(updatedDateMillis: Long) {
        currentDate = Date(updatedDateMillis)
        updateDateTextView()
    }


    fun searchEventsItineraryDay(day: String) {
        Log.d(">>>", "SE MODIFICO LA FECHA")
        Log.d(">>>", day)

        Firebase.firestore.collection("users")
            .document("BGlarOVaQGXPgMG72rQMZ1oOLhx1")
            .collection("itinerary")
            .document("cHu0EhWU6omgBC81PtbN")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                documentSnapshot?.let {
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
                                        showFragment(itineraryEmpty)
                                    } else {
                                        Log.d("ItineraryViewModel", "Hay eventos")
                                        showFragmentWithAdapter(itineraryFull,adapter)
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