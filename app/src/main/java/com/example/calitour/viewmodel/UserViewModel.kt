package com.example.calitour.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calitour.model.DTO.UserDTO
import com.example.calitour.model.entity.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    val _user = MutableLiveData<UserDTO>()


    fun getUser(){
        viewModelScope.launch(Dispatchers.IO) {
            val user = Firebase.firestore.collection("users")
                .document(Firebase.auth.currentUser!!.uid.toString())
                .get().addOnSuccessListener {
                    val foundUser = UserDTO(it.get("id") as String
                        ,it.get("birthday") as String
                        ,it.get("email") as String
                        ,it.get("name") as String
                        ,it.get("phoneNumber") as String
                        ,it.get("points") as Long
                        ,it.get("photoID") as String)

                    _user.value = foundUser
                }.addOnFailureListener{
                    Log.e(">>>", it.message.toString())
                }

        }
    }




}