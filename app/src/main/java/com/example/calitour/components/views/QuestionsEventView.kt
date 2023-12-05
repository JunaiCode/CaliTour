package com.example.calitour.components.views

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.calitour.databinding.ItineraryEventComponentBinding
import com.example.calitour.databinding.QuestionComponentBinding

class QuestionsEventView(root: View) : RecyclerView.ViewHolder(root){
    private val binding = QuestionComponentBinding.bind(root)
    val title = binding.titleTV
    val editQuestionBtn = binding.edtiBtn
}