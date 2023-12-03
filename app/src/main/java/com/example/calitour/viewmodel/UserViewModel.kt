package com.example.calitour.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calitour.model.DTO.EventFullDTO
import com.example.calitour.model.DTO.UserDTO
import com.example.calitour.model.repository.EventRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserViewModel : ViewModel() {

    val _user = MutableLiveData<UserDTO>()
    var eventsQuery = MutableLiveData<ArrayList<EventFullDTO>>()
    var eventRepo = EventRepository()

    fun getUser(){
        viewModelScope.launch(Dispatchers.IO) {
            var foundUser: UserDTO? = null
            val user = Firebase.firestore.collection("users")
                .document(Firebase.auth.currentUser!!.uid.toString())
                .get().addOnSuccessListener {
                    foundUser = it.toObject(UserDTO::class.java)
                    /*val foundUser = UserDTO(it.get("id") as String
                        ,it.get("birthday") as String
                        ,it.get("email") as String
                        ,it.get("name") as String
                        ,it.get("phoneNumber") as String
                        ,it.get("points") as Long
                        ,it.get("photoID") as String)

                    _user.value = foundUser*/
                }.addOnFailureListener{
                    Log.e(">>>", it.message.toString())
                }.await()
            withContext(Dispatchers.Main){
                _user.value=foundUser!!
            }

        }
    }

    fun reactToEvent(eventId: String){
        viewModelScope.launch (Dispatchers.IO) {
            eventRepo.reactToEvent(eventId)
        }
    }
    fun getAllActiveEvents(){
        viewModelScope.launch  (Dispatchers.IO){
            eventsQuery.postValue(eventRepo.getAllActiveEvents())
        }
    }




}