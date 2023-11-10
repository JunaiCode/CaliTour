package com.example.calitour.components.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.calitour.R
import com.example.calitour.components.views.HistoricEventView
import com.example.calitour.model.entity.Event

class HistoricEventAdapter : Adapter<HistoricEventView>() {
    val events =ArrayList<Event>()


    init {
        events
        //TODO(make coroutine to get information)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricEventView {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.historic_event_component, null, false)
        return HistoricEventView(view)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: HistoricEventView, position: Int) {
        val data = events[position]
        holder.eventTitle.text = data.name
        holder.eventSummary.text = data.description

    }


}