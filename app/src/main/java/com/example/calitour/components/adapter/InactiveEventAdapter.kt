package com.example.calitour.components.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.calitour.R
import com.example.calitour.components.views.InactiveEventViewHolder
import com.example.calitour.model.DTO.EventDocumentDTO
class InactiveEventAdapter: Adapter<InactiveEventViewHolder>() {
    private var inactiveEvents : ArrayList<EventDocumentDTO> = arrayListOf()

    init {

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InactiveEventViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        //XML -- View
        val view : View = layoutInflater.inflate(R.layout.entity_recent_event_card,parent,false)
        val holder = InactiveEventViewHolder(view)
        return holder
    }



    override fun onBindViewHolder(holder: InactiveEventViewHolder, position: Int) {
        holder.date.text = inactiveEvents[position].date.toString()
        holder.place.text = inactiveEvents[position].place
        holder.price.text = "Multiples precios"
        holder.title.text = inactiveEvents[position].name
    }
    override fun getItemCount(): Int {
        return inactiveEvents.size
    }

    fun setList(list:ArrayList<EventDocumentDTO>){
        inactiveEvents = list
        //Notificar RV
        notifyDataSetChanged()
    }
}