package com.example.calitour.components.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.calitour.R
import com.example.calitour.components.viewHolders.HistoricEventView
import com.example.calitour.model.DTO.EventDTO
import com.example.calitour.model.entity.Event
import com.google.firebase.Timestamp

class HistoricEventAdapter : Adapter<HistoricEventView>() {
    val events =ArrayList<EventDTO>()


    init {
        events.add(EventDTO("","", Timestamp.now(),
            "Puesta en escena de el increible gruop de salsa",
            "","Salsaton","",0,0,"",
            "https://th.bing.com/th/id/OIP.1Hd4WYtwR8w5VjdOAIkB5gHaHa?rs=1&pid=ImgDetMain"))

        events.add(EventDTO("","", Timestamp.now(),
            "Concierto de Rock en las afueras de la ciudad",
            "","Rock Calidoso","",0,0,"",
            "https://th.bing.com/th/id/R.1e63b319a312929e760de66f09ff6521?rik=H9eB01Wz5SRIAA&pid=ImgRaw&r=0"))

        events.add(EventDTO("","", Timestamp.now(),
            "Preparate para dar paso al mejor teatro marginal",
            "","Dramaticos","",0,0,"",
            "https://th.bing.com/th/id/OIP.t9x0l0FL7qSzyS_QJA9m4wHaE7?rs=1&pid=ImgDetMain"))

        events.add(EventDTO("","", Timestamp.now(),
            "Concurso de memes para darle paso al fin de semana",
            "","Memento","",0,0,"",
            "https://cdn.stackward.com/wp-content/uploads/2016/09/you-dont-say.jpg"))
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

        Glide.with(holder.itemView)
            .load(data.img)
            .into(holder.eventImage)
    }


}