package com.example.calitour.components.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.calitour.databinding.HistoricEventComponentBinding

class HistoricEventView(root:View) : ViewHolder(root) {
    private val binding = HistoricEventComponentBinding.bind(root)

    val eventTitle = binding.eventTitle
    val eventSummary = binding.eventSummary
    val eventImage = binding.entityImg
    val eventStar1 = binding.star1
    val eventStar2 = binding.star2
    val eventStar3 = binding.star3
    val eventStar4 = binding.star4
    val eventSar5 = binding.star5
    val selfComment = binding.selfComment
    val likeButton = binding.likeButton
    val commentButton = binding.commentButton
}