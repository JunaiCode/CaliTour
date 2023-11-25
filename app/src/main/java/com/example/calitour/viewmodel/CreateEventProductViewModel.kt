package com.example.calitour.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calitour.model.entity.EntityProduct
import com.example.calitour.model.entity.Event
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateEventProductViewModel: ViewModel() {

    fun createEvent(){
        viewModelScope.launch(Dispatchers.IO){

        }
    }

    fun createProduct(product:EntityProduct){

    }

    fun uploadImage(){

    }
}