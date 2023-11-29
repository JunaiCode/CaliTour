package com.example.calitour.components.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.calitour.R
import com.example.calitour.components.views.ActiveEventViewHolder
import com.example.calitour.model.DTO.EventDocumentDTO

class ActiveEventAdapter: Adapter<ActiveEventViewHolder>() {
    private var activeEvents : ArrayList<EventDocumentDTO> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveEventViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        //XML -- View
        val view : View = layoutInflater.inflate(R.layout.entity_event_card,parent,false)
        val holder = ActiveEventViewHolder(view)
        return holder
    }



    override fun onBindViewHolder(holder: ActiveEventViewHolder, position: Int) {

    }
    override fun getItemCount(): Int {
        return activeEvents.size
    }
}