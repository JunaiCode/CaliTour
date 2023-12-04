package com.example.calitour.activities.fragments

import DatePickerFragment
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calitour.R
import com.example.calitour.components.adapter.ItineraryEventAdapter
import com.example.calitour.components.adapter.UserEventAdapter
import com.example.calitour.databinding.ItineraryFragmentBinding
import com.example.calitour.model.DTO.EventItineraryDTO
import com.example.calitour.viewmodel.ItineraryViewModel
import com.example.calitour.viewmodel.UserViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ItineraryFragment : Fragment() {
    private lateinit var adapter: ItineraryEventAdapter
    private var currentDate: Date = Calendar.getInstance().time
    private lateinit var binding: ItineraryFragmentBinding
    private var eventsItinerary = ArrayList<EventItineraryDTO>()

    val vm: ItineraryViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItineraryFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateDateTextView()

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

        binding.reminderBtn.setOnClickListener {
            var numEvents = adapter.itemCount
            var title =  "Eventos para este dia"
            var description = "Tengo $numEvents eventos para asistir"
            val startTime = currentDate.time
            addReminderToCalendar(title, description, startTime, startTime)
        }

    }



    fun addReminderToCalendar(title: String, description: String, startTime: Long, endTime: Long) {
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
            .putExtra(CalendarContract.Events.TITLE, title)
            .putExtra(CalendarContract.Events.DESCRIPTION, description)
            .putExtra(CalendarContract.Events.ALL_DAY,true)
            .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)

        startActivity(intent)
    }

    fun convertToFormattedDate(inputDate: String): String {
        //Mon Dec 04 17:25:34 GMT 2023 en ingles
        //Mon Dec 04 12:26:45 GMT-05:00 2023 en español
        val inputFormatEn = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT' yyyy", Locale.US)
        val inputFormatEs = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH)
        Log.i("lo de la fechaaaa",inputDate)
        val currentLocales = context?.resources?.configuration?.locales
        Log.i("IDIOMA", currentLocales.toString())
        //[en_US]
        //[es_US]
        if(currentLocales.toString()=="[en_US]"){
            Log.i("IDIOMA","Esta en ingles")
            val date = inputFormatEn.parse(inputDate) ?: return ""
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return outputFormat.format(date)
        }else if(currentLocales.toString()=="[es_US]"){
            Log.i("IDIOMA","Esta en Español")
            val date = inputFormatEs.parse(inputDate) ?: return ""
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return outputFormat.format(date)
        }
        return ""
    }


    /*fun convertToFormattedDate(inputDate: String): String {
        //Mon Dec 04 17:25:34 GMT 2023 en ingles
        //Mon Dec 04 12:26:45 GMT-05:00 2023 en español
        val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.US)
        Log.i("lo de la fechaaaa", inputDate)

        try {
            // Si hay un guion antes de la zona horaria, reemplácelo con espacio para manejar ambos casos
            val formattedInputDate = inputDate.replace("GMT-","GMT- ")

            val date = inputFormat.parse(formattedInputDate) ?: return ""
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            return ""
        }
    }*/



    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateDateTextView() {
        if (::binding.isInitialized && view != null) {
            val dateFormat = SimpleDateFormat("EEE, MMM dd / yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(currentDate)
            binding.pageDateTV.text = formattedDate
            showEmptyPage()
            adapter = ItineraryEventAdapter()
            var day = convertToFormattedDate(currentDate.toString())
            Log.i("lo de la fechaaaa 22222", day)
            if (viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
                Log.i("---------", day)
                vm.getEventsItineraryByDate(day).observe(viewLifecycleOwner) { events ->
                    adapter.updateData(events)
                    Log.i("itemcount", adapter.itemCount.toString())
                    if (view != null) {
                        if (events.isEmpty()) {
                            showEmptyPage()
                        } else {
                            showFullPage()
                        }
                    }
                }
            }else{
                vm.getEventsItineraryByDate(day).observe(viewLifecycleOwner) { events ->
                    adapter.updateData(events)
                    if(adapter.itemCount==0){
                        showEmptyPage()
                    }else{
                        showFullPage()
                    }
                }
            }
        }
    }

    private fun showEmptyPage(){
        var recyclerView = binding.itineraryEventsRecyclerView
        binding.reminderBtn.visibility = View.INVISIBLE
        binding.itineraryFullTitleTV.visibility = View.INVISIBLE
        recyclerView.visibility = View.INVISIBLE
        binding.itineraryEmptyTV.visibility = View.VISIBLE

    }

    private fun showFullPage(){
        var recyclerView = binding.itineraryEventsRecyclerView
        binding.reminderBtn.visibility = View.VISIBLE
        binding.itineraryFullTitleTV.visibility = View.VISIBLE
        recyclerView.visibility = View.VISIBLE
        binding.itineraryEmptyTV.visibility = View.INVISIBLE
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
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

    private fun openDatePicker() {
        val datePickerFragment = DatePickerFragment()
        datePickerFragment.itineraryFragment = this

        val bundle = Bundle()
        bundle.putLong(DatePickerFragment.SELECTED_DATE_KEY, currentDate.time)
        datePickerFragment.arguments = bundle

        datePickerFragment.show(parentFragmentManager, DatePickerFragment.TAG)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDate(updatedDateMillis: Long) {
        currentDate = Date(updatedDateMillis)
        updateDateTextView()
    }


}