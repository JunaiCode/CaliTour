package com.example.calitour.activities.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calitour.R
import com.example.calitour.components.adapter.ItineraryEventAdapter

class ItineraryFullFragment : Fragment() {
    private lateinit var adapter: ItineraryEventAdapter
    private lateinit var recyclerView: RecyclerView

    fun setAdapter(adapter: ItineraryEventAdapter) {
        this.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.itinerary_full_fragment, container, false)
        recyclerView = view.findViewById(R.id.itineraryEventsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        if (::recyclerView.isInitialized) {
            recyclerView.adapter = adapter
        }
        return view
    }
}
