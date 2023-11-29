package com.example.calitour.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calitour.model.entity.EntityProduct
import com.example.calitour.model.repository.EventRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class EntityViewModel:ViewModel() {

    var completedProduct = MutableLiveData<Boolean>()
    var eventRepo = EventRepository()

    fun createProduct(product: EntityProduct, entityId: String){

        viewModelScope.launch(Dispatchers.IO) {

            val productId = UUID.randomUUID().toString()

            val newProduct = hashMapOf(
                "description" to product.description,
                "name" to product.name,
                "points" to product.price,
                "id" to productId,
                "image" to ""
            )

            Firebase.firestore.collection("entities")
                .document(entityId)
                .collection("products")
                .document(productId)
                .set(newProduct)
                .await()

            if(product.imageUri != Uri.parse("")){
                uploadImage(product.imageUri,entityId, productId)
            }

            withContext(Dispatchers.Main) {
                completedProduct.value = true
            }

        }
    }

    fun getAllEvents(){
        viewModelScope.launch (Dispatchers.IO){
            val events = eventRepo.getAllEvents()
        }
    }

    fun getEventsById(id:String){
        viewModelScope.launch (Dispatchers.IO){
            val events = eventRepo.getEventByEntityId(id)
            Log.e("Eventos de esta entidad: ",events.toString())
        }
    }

    fun getEventsUnavailablesByEntityId(id:String){
        viewModelScope.launch (Dispatchers.IO){
            val events = eventRepo.getEventsUnavailablesByEntityId(id)
            Log.e("Eventos inactivos de esta entidad: ",events.toString())
        }
    }

    fun getEventsAvailablesByEntityId(id:String){
        viewModelScope.launch (Dispatchers.IO){
            val events = eventRepo.getEventsAvailablesByEntityId(id)
            Log.e("Eventos activos de esta entidad: ",events.toString())
        }
    }

    fun uploadImage(uri: Uri, entityId:String, productId:String){
        viewModelScope.launch (Dispatchers.IO) {
            try {
                val uuid = UUID.randomUUID().toString()
                Firebase.storage.reference
                    .child("productImages")
                    .child(uuid)
                    .putFile(uri).await()

                Firebase.firestore.collection("entities")
                    .document(entityId)
                    .collection("products")
                    .document(productId)
                    .update("image", uuid)
                    .await()

            }catch (ex:Exception){
                Log.e("<<<<<", ex.message.toString())
            }

        }
    }

}