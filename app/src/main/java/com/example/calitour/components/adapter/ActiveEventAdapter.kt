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
import com.bumptech.glide.Glide
import com.example.calitour.R
import com.example.calitour.activities.CreateEventProductActivity
import com.example.calitour.activities.ProfileEntityActivity
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
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.UUID

class ActiveEventAdapter: Adapter<ActiveEventViewHolder>() {
    private var activeEvents : ArrayList<EventDocumentDTO> = arrayListOf()
    private var uris :ArrayList<Uri> = arrayListOf()
    private var prices :ArrayList<String> = arrayListOf()
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
        val sdf = SimpleDateFormat("dd/MM - HH:mm a")
        val date = sdf.format(activeEvents[position].date.toDate())
        holder.date.text = date
        holder.place.text = activeEvents[position].place
        holder.title.text = activeEvents[position].name
        if(prices.size>0){
            if(prices[position] == "0"){
                holder.price.text = holder.itemView.context.getString(R.string.free)
            }else{
                holder.price.text = prices[position]
            }
        }
        if(uris.size>0){
            Glide.with(holder.itemView.context).load(uris[position]).into(holder.img)
        }
        holder.editBtn.setOnClickListener{
            holder.editBtn.setBackgroundResource(R.drawable.edit)
            holder.deleteBtn.setBackgroundResource(R.drawable.delete_icon)
            val intent = Intent(holder.itemView.context, CreateEventProductActivity::class.java).putExtra("fragment", "EDIT_EVENT").putExtra("id",activeEvents[position].id)
            holder.itemView.context.startActivity(intent)
            holder.editBtn.setBackgroundResource(R.drawable.edit_icon)
        }
        holder.deleteBtn.setOnClickListener{
            holder.editBtn.setBackgroundResource(R.drawable.edit_icon)
            holder.deleteBtn.setBackgroundResource(R.drawable.delete)
            vm.deleteEvent(activeEvents[position])
            activeEvents.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    override fun getItemCount(): Int {
        return activeEvents.size
    }

    fun setList(list:ArrayList<EventDocumentDTO>){
        activeEvents = list
        notifyDataSetChanged()
    }

    fun setUris(uriArray:ArrayList<Uri>){
        uris = uriArray
        notifyDataSetChanged()
    }

    fun setPrices(pricesArray:ArrayList<String>){
        prices = pricesArray
        notifyDataSetChanged()
    }
}