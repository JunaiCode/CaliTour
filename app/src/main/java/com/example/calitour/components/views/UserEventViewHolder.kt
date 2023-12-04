package com.example.calitour.components.views

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.calitour.databinding.UserEventCardBinding

class UserEventViewHolder(root: View): ViewHolder(root) {

    private val binding = UserEventCardBinding.bind(root)
    val eventName = binding.activeEventTitle
    val eventPrice = binding.activePriceEvent
    val eventEntity = binding.entityEventName
    val eventLocation = binding.locationActiveEvent
    val eventDate = binding.dateActiveEvent
    val image = binding.imagenFondo
    val interestBtn = binding.interestBtn

}