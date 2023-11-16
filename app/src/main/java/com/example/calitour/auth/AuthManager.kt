package com.example.calitour.auth


import android.util.Log

import com.example.calitour.model.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AuthManager {



    fun registerNewUser( email:String, password:String, name: String, birthday : String, phoneNumber:String) {

        Firebase.auth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {

                val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser;
                val userId : String = firebaseUser!!.uid

                val user = User(userId,birthday,email,name,phoneNumber,"${0}")



                Firebase.firestore.collection("users").document(userId).set(user)
                Log.e("user", it.user.toString())


            }
            .addOnFailureListener{
               Log.e("Error", it.message.toString())
            }


    }




    fun loginManager(email: String, password: String) : User?{
        var theUser : User? = User("","","","","","")
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                var user = Firebase.auth.currentUser

                Firebase.firestore.collection("users").document(user!!.uid).get().addOnSuccessListener {
                    val usr = it.toObject(User::class.java)
                    Log.e("usuario",it.id)
                }

            }.addOnFailureListener{
                Log.e("usuario",it.message.toString())
                theUser = null
            }

        return theUser
    }


}