package com.example.calitour.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.calitour.model.DTO.EventDocumentDTO
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventsViewModel:EntityViewModel() {

    fun getEvents(){
        viewModelScope.launch(Dispatchers.IO) {
            val collectionRef = Firebase.firestore.collection("events")
            collectionRef.get()
                .addOnSuccessListener { querySnapshot ->
                    val events = mutableListOf<EventDocumentDTO>()
                    for(document in querySnapshot.documents){
                        val event = document.toObject(EventDocumentDTO::class.java)
                        if(event!=null){
                            events.add(event)
                        }
                        Log.e(">>>", events.toString())
                    }


                }
        }


    }
}