package com.example.calitour.components.adapter

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calitour.R
import com.example.calitour.components.views.TriviasEventView
import com.example.calitour.model.DTO.EventTriviaDTO
import com.example.calitour.model.DTO.QuestionDTO
import com.example.calitour.viewmodel.EntityViewModel

class TriviasEventAdapter: RecyclerView.Adapter<TriviasEventView>() {
    private var triviaEvents : ArrayList<EventTriviaDTO> = arrayListOf()
    private var uris :ArrayList<Uri> = arrayListOf()

    private lateinit var adapter: QuestionsEventAdapter

    private val vm = EntityViewModel()

    private var eventId=""

    init {

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TriviasEventView {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        //XML -- View
        val view : View = layoutInflater.inflate(R.layout.entity_event_trivia_card,parent,false)
        val holder = TriviasEventView(view)
        return holder
    }



    override fun onBindViewHolder(holder: TriviasEventView, position: Int) {

        val data = triviaEvents[position]
        holder.name.text = data.name
        holder.numQuestions.text = 1.toString()

        if(uris.size>0){
            Glide.with(holder.itemView.context).load(uris[position]).into(holder.img)
        }
        holder.manageQuestionsBtn.setOnClickListener{
            showPopupTrivias(holder.itemView.context, data)
        }
    }

    private fun showPopupTrivias(context: Context, data: EventTriviaDTO) {
        adapter = QuestionsEventAdapter()
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.popup_trivia)
        val newQuestion = dialog.findViewById<Button>(R.id.addQuestionBtn)
        val save = dialog.findViewById<Button>(R.id.saveBtn)
        val questionsRV = dialog.findViewById<RecyclerView>(R.id.questionsRecyclerView)
        questionsRV.layoutManager = LinearLayoutManager(context)
        questionsRV.adapter = adapter

        Log.i("ID del evento",data.id)
        eventId=data.id
        vm.getQuestionsByEventId(data.id)
        vm.questionsQuery.observe(context as LifecycleOwner) { questions ->
            // Actualizar la interfaz de usuario según sea necesario con las nuevas preguntas
            Log.i("TriviasEventAdapter", "questionsQuery changed: $questions")
            adapter.setEventId(data.id)
            adapter.updateData(questions)
        }
        newQuestion.setOnClickListener {
            showPopupQuestion(context)
            dialog.dismiss()
        }
        save.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showPopupQuestion(context: Context) {

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.popup_question)
        val title = dialog.findViewById<EditText>(R.id.questionET)
        val option1 = dialog.findViewById<EditText>(R.id.option1ET)
        val option2 = dialog.findViewById<EditText>(R.id.option2ET)
        val option3 = dialog.findViewById<EditText>(R.id.option3ET)
        val option4 = dialog.findViewById<EditText>(R.id.option4ET)
        val saveBtn = dialog.findViewById<Button>(R.id.saveBtn)

        title.setText("Pregunta")
        option1.setText("Respuesta")
        option2.setText("Opcion")
        option3.setText("Opcion")
        option4.setText("Opcion")

        saveBtn.setOnClickListener {
            val updatedTitle = title.text.toString()
            val updatedOptions = listOf(
                option1.text.toString(),
                option2.text.toString(),
                option3.text.toString(),
                option4.text.toString()
            )
            val correctAns=option1.text.toString()
            Log.i("QuestionDTO Created","se esta creando")
            vm.addQuestion(eventId, updatedTitle, updatedOptions,correctAns)
            dialog.dismiss()
        }
        dialog.show()
    }


    override fun getItemCount(): Int {
        return triviaEvents.size
    }

    fun setList(list:ArrayList<EventTriviaDTO>){
        triviaEvents = list
        notifyDataSetChanged()
    }

    fun setUris(uriArray:ArrayList<Uri>){
        uris = uriArray
        notifyDataSetChanged()
    }
}