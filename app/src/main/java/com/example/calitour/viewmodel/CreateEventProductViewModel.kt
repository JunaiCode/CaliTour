package com.example.calitour.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calitour.model.DTO.BadgeDTO
import com.example.calitour.model.DTO.EventDocumentDTO
import com.example.calitour.model.DTO.PriceDTO
import com.example.calitour.model.entity.EntityProduct
import com.example.calitour.model.entity.Event
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateEventProductViewModel: ViewModel() {

    fun createEvent(e: Event) {
        val eventDto = EventDocumentDTO(e.id.toString(),e.category,e.date,e.description,e.entityId.toString(),e.name,e.place,e.reaction,e.score,e.state,e.img)
        val priceDTO = PriceDTO(e.prices[0].description,e.prices[0].fee,e.prices[0].id.toString(),e.prices[0].name)
        val badgeDTO = BadgeDTO(e.badges[0].id.toString(),e.badges[0].img,e.badges[0].name)
        viewModelScope.launch(Dispatchers.IO){
            Firebase.firestore.collection("events").document(eventDto.id.toString()).set(eventDto)
            Firebase.firestore.collection("events").document(eventDto.id.toString()).collection("prices").document(
                priceDTO.id).set(priceDTO)
            Firebase.firestore.collection("events").document(eventDto.id.toString()).collection("badges").document(
                badgeDTO.id).set(badgeDTO)
        }
    }

    fun createProduct(product:EntityProduct){

    }

    fun uploadImage(){

    }
}