package com.example.calitour.activities.fragments

import DatePickerFragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calitour.R
import com.example.calitour.components.adapter.ItineraryEventAdapter
import com.example.calitour.databinding.ItineraryFragmentBinding
import com.example.calitour.model.DTO.EventDocumentDTO
import com.example.calitour.model.entity.ItineraryDay
import com.example.calitour.model.entity.ItineraryEvent
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ItineraryFragment : Fragment() {
    lateinit var adapter: ItineraryEventAdapter
    private var itineraryFull = ItineraryFullFragment()
    private val itineraryEmpty = ItineraryEmptyFragment()
    private var currentDate: Date = Calendar.getInstance().time
    private lateinit var binding: ItineraryFragmentBinding

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
            var userId = Firebase.auth.currentUser!!.uid
            searchEventsItineraryDay(userId,currentDate.toString())
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
            val args = Bundle()
            args.putLong("date", currentDate.time)
            fragment.arguments = args
        }

        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentEventsItineraryContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
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

    fun searchEventsItineraryDay(userId:String, day: String) {

        adapter= ItineraryEventAdapter()
        val userItineraryRef = Firebase.firestore.collection("users").document(userId).collection("itinerary")
        var dayString = convertToFormattedDate(day)

        userItineraryRef
            .whereEqualTo("dayString", dayString) // Ajusta el nombre del campo según tu estructura
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    showFragment(itineraryEmpty)
                } else {
                    for (document in querySnapshot) {
                        val documentId = document.id
                        userItineraryRef
                            .document(documentId)
                            .get()
                            .addOnSuccessListener { documentSnapshot ->
                                documentSnapshot?.let {
                                    val jsonData = Gson().toJson(it.data)
                                    val itineraryDay = Gson().fromJson(jsonData, ItineraryDay::class.java)
                                    loadEventDetails(itineraryDay.events)
                                }
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.d("Documento no encontrado con ID:", e.toString())
            }
    }

    fun loadEventDetails(eventsIdList: ArrayList<String>) {
        for (eventId in eventsIdList) {
            Firebase.firestore.collection("events")
                .document(eventId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val jsonDataEvent = Gson().toJson(documentSnapshot.data)
                        val eventDocumentDTO = Gson().fromJson(jsonDataEvent, EventDocumentDTO::class.java)
                        val pricesCollection = documentSnapshot.reference.collection("prices")
                        pricesCollection.whereEqualTo("name", "General")
                            .get()
                            .addOnSuccessListener { pricesQuerySnapshot ->
                                for (priceDocument in pricesQuerySnapshot.documents) {
                                    val fee = priceDocument.getDouble("fee")
                                    val event = eventDocumentDTO
                                    val itineraryEvent = fee?.let {
                                        ItineraryEvent(
                                            name = event.name,
                                            place = event.place,
                                            eventTime = convertTimestampToFormattedTime(event.date),
                                            img = event.img,
                                            price = it
                                        )
                                    }

                                    if (itineraryEvent != null) {
                                        adapter.addEvent(itineraryEvent)
                                    }
                                    if (adapter.itemCount != 0) {
                                        itineraryFull = ItineraryFullFragment()
                                        showFragmentWithAdapter(itineraryFull, adapter)
                                    }
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.e("loadEventDetails", "Error al obtener datos de la subcolección prices", e)
                            }

                    } else {
                        Log.e("loadEventDetails", "No existe un evento con ID: $eventId")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("loadEventDetails", "Error al obtener datos para el evento con ID: $eventId", e)
                }
        }
    }

    fun convertTimestampToFormattedTime(timestamp: Timestamp): String {
        val date = Date(timestamp.seconds * 1000)
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(date)
    }

    fun convertToFormattedDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH)
        val date = inputFormat.parse(inputDate) ?: return ""
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return outputFormat.format(date)
    }
}