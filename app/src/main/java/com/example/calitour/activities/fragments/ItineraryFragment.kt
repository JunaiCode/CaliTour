package com.example.calitour.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calitour.R

class ItineraryFragment: Fragment() {

    lateinit var adapter: EventAdapter
    private val menuEntityEventsVM: MenuEntityEventsViewModel by viewModels()


    private val binding by lazy{
        ActivityMenuEntityEventsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter = EventAdapter()
        binding.entityEventsList.layoutManager = LinearLayoutManager(this)
        binding.entityEventsList.setHasFixedSize(true)
        binding.entityEventsList.adapter = adapter

        menuEntityEventsVM.getAllEvents()
        menuEntityEventsVM.events.observe(this, { events ->
            adapter.updateData(events)
        })
    }
}