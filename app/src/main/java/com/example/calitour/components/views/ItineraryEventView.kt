package com.example.calitour.components.views

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.calitour.databinding.ItineraryEventComponentBinding

class ItineraryEventView(root: View) : RecyclerView.ViewHolder(root){
    private val binding = ItineraryEventComponentBinding.bind(root)
    val nameEvent = binding.nameTV
    val timeEvent = binding.timeTV
    val priceEvent = binding.priceTV
    val placeEvent = binding.placeTV
    val image = binding.eventImageView
    val card = binding.itineraryEventCV
}