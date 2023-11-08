package com.example.calitour.components.views

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.calitour.databinding.HistoricEventComponentBinding

class HistoricEventView(root:View) : ViewHolder(root) {
    private val binding = HistoricEventComponentBinding.bind(root)

    val eventTitle = binding.eventTitle
    val eventSummary = binding.eventSummary
}