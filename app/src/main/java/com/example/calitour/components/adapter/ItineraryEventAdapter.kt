package com.example.calitour.components.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.calitour.R
import com.example.calitour.components.views.ItineraryEventView
import com.example.calitour.model.entity.ItineraryEvent

class ItineraryEventAdapter : Adapter<ItineraryEventView>() {
    val events = ArrayList<ItineraryEvent>()

    fun updateData(newItems: List<ItineraryEvent>) {
        events.clear()
        events.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItineraryEventView {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.itinerary_event_component, parent, false)
        val itineraryEventView = ItineraryEventView(view)
        return itineraryEventView
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: ItineraryEventView, position: Int) {
        val data = events[position]
        holder.nameEvent.text = data.name
        holder.timeEvent.text = data.eventTime
        holder.priceEvent.text = "$ ${data.price}"
        holder.placeEvent.text = data.place
    }

    fun addEvent(event:ItineraryEvent){
        events.add(event)
        notifyDataSetChanged()
    }
}
