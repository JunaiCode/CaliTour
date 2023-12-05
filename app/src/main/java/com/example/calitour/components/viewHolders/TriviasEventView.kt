package com.example.calitour.components.views

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.calitour.databinding.EntityEventTriviaCardBinding

class TriviasEventView (root: View): RecyclerView.ViewHolder(root) {
    private val binding = EntityEventTriviaCardBinding.bind(root)
    val entityName = binding.entityEventNameTV
    val name = binding.eventTitleTV
    val numQuestions = binding.numTriviasTV
    val img = binding.imagenFondo
    val manageQuestionsBtn = binding.manageBtn
}