package com.example.calitour.activities.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calitour.components.adapter.ItineraryEventAdapter
import com.example.calitour.databinding.ItineraryFullFragmentBinding
import android.content.Intent
import android.provider.CalendarContract
import android.provider.CalendarContract.Events
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class ItineraryFullFragment : Fragment() {
    private lateinit var adapter: ItineraryEventAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ItineraryFullFragmentBinding
    private lateinit var currentDate: Date

    fun setAdapter(adapter: ItineraryEventAdapter) {
        this.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val dateMillis: Long? = arguments?.getLong("date")
        currentDate = dateMillis?.let { Date(it) } ?: Date()

        binding = ItineraryFullFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.itineraryEventsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

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
            .setData(Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
            .putExtra(Events.TITLE, title)
            .putExtra(Events.DESCRIPTION, description)
            .putExtra(Events.ALL_DAY,true)
            .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)

        startActivity(intent)
    }
}
