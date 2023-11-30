package com.example.calitour.components.views

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.calitour.databinding.EntityEventCardBinding

class ActiveEventViewHolder(root:View): ViewHolder(root) {
    private val binding = EntityEventCardBinding.bind(root)
    val title = binding.activeEventTitle
    val entityName = binding.entityEventName
    val price = binding.activePriceEvent
    val place = binding.locationActiveEvent
    val date = binding.dateActiveEvent
    val img = binding.imagenFondo
}