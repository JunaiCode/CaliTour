package com.example.calitour.components.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calitour.R
import com.example.calitour.components.views.ItineraryEventView
import com.example.calitour.model.entity.Event
import java.text.SimpleDateFormat
import java.util.Locale

class ItineraryEventAdapter : RecyclerView.Adapter<ItineraryEventView>() {
    val events = ArrayList<Event>()

    fun updateData(newItems: List<Event>) {
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
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeHour = timeFormat.format(data.date)
        holder.timeEvent.text = timeHour
        holder.priceEvent.text = "$0.00"   //falta acceder al precio
        holder.placeEvent.text = data.place
    }
}
