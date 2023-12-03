package com.example.calitour.components.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.calitour.R
import com.example.calitour.components.views.UserEventViewHolder
import com.example.calitour.model.DTO.EventFullDTO
import java.text.SimpleDateFormat

class UserEventAdapter: Adapter<UserEventViewHolder>() {

    private var events: ArrayList<EventFullDTO> = arrayListOf()
    private var clickListener: ItemClickListener? = null
    private var interestClick: Boolean = false

    fun setClickListener(itemClickListener: ItemClickListener) {
        clickListener = itemClickListener
    }

    interface ItemClickListener {
        fun interestClickListener(entityId: String, operation: String)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserEventViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.user_event_card, parent, false)
        return UserEventViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserEventViewHolder, position: Int) {
        val event = events[position]
        val sdf = SimpleDateFormat("dd/MM - HH:mm a")
        val date = sdf.format(event.date.toDate())

        if(event.img != ""){
            Glide.with(holder.itemView.context)
                .load(event.img)
                .into(holder.image)
        }

        holder.interestBtn.setOnClickListener {
            if(interestClick){
                holder.interestBtn.setImageResource(R.drawable.star_gray)
                var reactions = event.reaction
                event.reaction = reactions--
                interestClick = false
                clickListener?.interestClickListener(event.id, "remove")
            }else {
                holder.interestBtn.setImageResource(R.drawable.star_yellow)
                var reactions = event.reaction
                event.reaction = reactions++
                interestClick = true
                clickListener?.interestClickListener(event.id, "add")
            }

        }
        holder.eventName.text = event.name
        holder.eventEntity.text = event.entityName
        holder.eventDate.text = date
        holder.eventLocation.text = event.place
        holder.eventPrice.text
    }

    override fun getItemCount(): Int {
        return events.size
    }


    fun setEvents(events: ArrayList<EventFullDTO>){
        this.events = events
        notifyDataSetChanged()
    }
}