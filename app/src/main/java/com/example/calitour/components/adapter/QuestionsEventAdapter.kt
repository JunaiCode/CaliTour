package com.example.calitour.components.adapter

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calitour.R
import com.example.calitour.components.views.QuestionsEventView
import com.example.calitour.model.DTO.EventTriviaDTO
import com.example.calitour.model.DTO.QuestionDTO
import com.example.calitour.viewmodel.EntityViewModel

class QuestionsEventAdapter : RecyclerView.Adapter<QuestionsEventView>() {
    val quesions = ArrayList<QuestionDTO>()

    private val vm = EntityViewModel()
    private var eventId: String = ""

    private var currentDialog: Dialog? = null // Variable para mantener el popup actual



    fun updateData(newItems: ArrayList<QuestionDTO>) {
        quesions.clear()
        quesions.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setEventId(id:String) {
        eventId = id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsEventView {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.question_component, parent, false)
        val questionsEventView = QuestionsEventView(view)
        return questionsEventView
    }

    override fun getItemCount(): Int {
        return quesions.size
    }

    override fun onBindViewHolder(holder: QuestionsEventView, position: Int) {
        val data = quesions[position]
        holder.title.text = data.title

        holder.editQuestionBtn.setOnClickListener {
            Log.i("INFORMACION DE LA PREGUNTA ", data.toString())
            showPopupQuestion(holder.itemView.context, data)
        }
    }

    private fun showPopupQuestion(context: Context, data: QuestionDTO) {

        currentDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.popup_question)
        val title = dialog.findViewById<EditText>(R.id.questionET)
        val option1 = dialog.findViewById<EditText>(R.id.option1ET)
        val option2 = dialog.findViewById<EditText>(R.id.option2ET)
        val option3 = dialog.findViewById<EditText>(R.id.option3ET)
        val option4 = dialog.findViewById<EditText>(R.id.option4ET)
        val saveBtn = dialog.findViewById<Button>(R.id.saveBtn)

        title.setText(data.title)
        option1.setText(data.options.get(0))
        option2.setText(data.options.get(1))
        option3.setText(data.options.get(2))
        option4.setText(data.options.get(3))

        saveBtn.setOnClickListener {
            val updatedTitle = title.text.toString()
            val updatedOptions = listOf(
                option1.text.toString(),
                option2.text.toString(),
                option3.text.toString(),
                option4.text.toString()
            )
            Log.i("QuestionDTO Updated", data.toString())
            vm.updateQuestion(eventId,data.id, updatedTitle, updatedOptions)
            dialog.dismiss()
        }
        dialog.show()
    }
}
