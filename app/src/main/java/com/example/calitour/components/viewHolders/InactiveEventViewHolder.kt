package com.example.calitour.components.views

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.calitour.databinding.EntityEventCardBinding
import com.example.calitour.databinding.EntityRecentEventCardBinding

class InactiveEventViewHolder(root: View): RecyclerView.ViewHolder(root) {
    private val binding = EntityRecentEventCardBinding.bind(root)
    val title = binding.inactiveEventTitle
    val price = binding.inactivePriceEvent
    val place = binding.locationInactiveEvent
    val date = binding.dateInactiveEvent
    val img = binding.imagenFondo
}