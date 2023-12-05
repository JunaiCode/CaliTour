package com.example.calitour.components.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.calitour.R
import com.example.calitour.activities.EventDetailActivity
import com.example.calitour.components.views.ItineraryEventView
import com.example.calitour.model.DTO.EventItineraryDTO
import com.example.calitour.model.entity.Event

class ItineraryEventAdapter : Adapter<ItineraryEventView>() {
    val events = ArrayList<EventItineraryDTO>()

    fun updateData(newItems: ArrayList<EventItineraryDTO>) {
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

        var price=0.0
        if(price == 0.0){
            holder.priceEvent.text = holder.itemView.context.getString (R.string.free)

        }else {
            holder.priceEvent.text = "$ ${price}"
        }
        if(data.img != ""){
            Glide.with(holder.itemView.context)
                .load(data.img)
                .into(holder.image)
        }
        holder.placeEvent.text = data.place

        holder.card.setOnClickListener{
            holder.itemView.context.startActivity(
                Intent(holder.itemView.context, EventDetailActivity::class.java).putExtra("event_id", data.id)
            )
        }
    }

    fun addEvent(event:EventItineraryDTO){
        events.add(event)
        //notifyDataSetChanged()
    }
}
