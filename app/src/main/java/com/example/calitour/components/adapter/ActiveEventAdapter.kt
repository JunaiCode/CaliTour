package com.example.calitour.components.adapter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.calitour.R
import com.example.calitour.activities.CreateEventProductActivity
import com.example.calitour.activities.fragments.ActiveEventFragment
import com.example.calitour.components.views.ActiveEventViewHolder
import com.example.calitour.model.DTO.BadgeDTO
import com.example.calitour.model.DTO.EventDocumentDTO
import com.example.calitour.model.entity.Badge
import com.example.calitour.model.entity.Event
import com.example.calitour.model.entity.Price
import com.example.calitour.model.entity.Trivia
import com.example.calitour.viewmodel.CreateEventProductViewModel
import com.example.calitour.viewmodel.EntityViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import java.util.UUID

class ActiveEventAdapter: Adapter<ActiveEventViewHolder>() {
    private var activeEvents : ArrayList<EventDocumentDTO> = arrayListOf()
    private val vm: CreateEventProductViewModel = CreateEventProductViewModel()
    init {

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveEventViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        //XML -- View
        val view : View = layoutInflater.inflate(R.layout.entity_event_card,parent,false)
        val holder = ActiveEventViewHolder(view)
        return holder
    }



    override fun onBindViewHolder(holder: ActiveEventViewHolder, position: Int) {
        holder.date.text = activeEvents[position].date.toString()
        holder.place.text = activeEvents[position].place
        holder.price.text = "Multiples precios"
        holder.title.text = activeEvents[position].name
        holder.editBtn.setOnClickListener{
            holder.editBtn.setBackgroundResource(R.drawable.edit)
            holder.deleteBtn.setBackgroundResource(R.drawable.delete_icon)
            val intent = Intent(holder.itemView.context, CreateEventProductActivity::class.java).putExtra("fragment", "EDIT_EVENT").putExtra("id",activeEvents[position].id)
            holder.itemView.context.startActivity(intent)
        }
        holder.deleteBtn.setOnClickListener{
            holder.editBtn.setBackgroundResource(R.drawable.edit_icon)
            holder.deleteBtn.setBackgroundResource(R.drawable.delete)
            vm.deleteEvent(activeEvents[position])

        }
    }
    override fun getItemCount(): Int {
        return activeEvents.size
    }

    fun setList(list:ArrayList<EventDocumentDTO>){
        activeEvents = list
        notifyDataSetChanged()
    }
}