package com.example.calitour.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calitour.model.entity.AuthState
import com.example.calitour.model.entity.ErrorMessage
import com.example.calitour.model.entity.User
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException

class AuthViewModel : ViewModel(){

    val authStateLV = MutableLiveData<AuthState>()
    val errorLV = MutableLiveData<ErrorMessage>()
    fun signup(user :User, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = Firebase.auth.createUserWithEmailAndPassword(user.email, pass).await()
                val newUser = hashMapOf(
                    "name" to user.name,
                    "email" to user.email,
                    "id" to Firebase.auth.currentUser?.uid,
                    "birthday" to user.birthday,
                    "phoneNumber" to user.phoneNumber,
                    "points" to user.points
                )
                Firebase.firestore.collection("users")
                    .document(newUser["id"]!!.toString())
                    .set(newUser).await()

                withContext(Dispatchers.Main){ authStateLV.value = AuthState(result.user?.uid, true)}
                Log.e(">>>", "Registrado")
                Firebase.auth.currentUser
            }catch (e: FirebaseAuthInvalidCredentialsException) {
                withContext(Dispatchers.Main){errorLV.value = ErrorMessage("El correo está mal formado")}
                Log.e(">>>", e.message!!)
            } catch (e: FirebaseAuthUserCollisionException) {
                withContext(Dispatchers.Main){errorLV.value = ErrorMessage("El correo está repetido")}
                Log.e(">>>", "Repetido")
            } catch (e: FirebaseAuthWeakPasswordException) {
                withContext(Dispatchers.Main){errorLV.value = ErrorMessage("La clave es muy debil")}
                Log.e(">>>", "Clave muy corta")
            }catch (e: IllegalArgumentException){
                withContext(Dispatchers.Main){errorLV.value = ErrorMessage("Llene los campos")}
                Log.e(">>>", "Campos")
            }
        }
    }



    fun signin(email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = Firebase.auth.signInWithEmailAndPassword(email, pass).await()
                withContext(Dispatchers.Main){authStateLV.value = AuthState(result.user?.uid, true)}
                Log.e(">>>", "Loggeado")
            } catch (e: FirebaseAuthException) {
                withContext(Dispatchers.Main){errorLV.value = ErrorMessage("Error de autenticación")}
                Log.e(">>>", "Error de autenticación")
            }
        }

    }
}