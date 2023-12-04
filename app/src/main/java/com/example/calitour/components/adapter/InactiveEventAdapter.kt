package com.example.calitour.components.adapter


import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.calitour.R
import com.example.calitour.components.views.InactiveEventViewHolder
import com.example.calitour.model.DTO.EventDocumentDTO
import com.example.calitour.viewmodel.CreateEventProductViewModel
import java.text.SimpleDateFormat

class InactiveEventAdapter: Adapter<InactiveEventViewHolder>() {
    private var inactiveEvents : ArrayList<EventDocumentDTO> = arrayListOf()
    private var uris :ArrayList<Uri> = arrayListOf()
    private val vm: CreateEventProductViewModel = CreateEventProductViewModel()
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
        val sdf = SimpleDateFormat("dd/MM - HH:mm a")
        val date = sdf.format(inactiveEvents[position].date.toDate())
        holder.date.text = date
        holder.place.text = inactiveEvents[position].place
        val price = vm.getPricesEvent(inactiveEvents[position].id).value?.get(0)?.fee?.toInt();
        if(price != 0){
            holder.price.text = price.toString()
        }else{
            holder.price.text = R.string.free.toString()
        }
        if(uris.size>0){
            Glide.with(holder.itemView.context).load(uris[position]).into(holder.img)
        }
        if(uris.size>0){
            Glide.with(holder.itemView.context).load(uris[position]).into(holder.img)
        }
    }
    override fun getItemCount(): Int {
        return inactiveEvents.size
    }

    fun setList(list:ArrayList<EventDocumentDTO>){
        inactiveEvents = list
        //Notificar RV
        notifyDataSetChanged()
    }

    fun setUris(uriArray:ArrayList<Uri>){
        uris = uriArray
        notifyDataSetChanged()
    }
}