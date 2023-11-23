package com.example.calitour.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calitour.model.entity.AuthState
import com.example.calitour.model.entity.Entity
import com.example.calitour.model.entity.ErrorMessage
import com.example.calitour.model.entity.User
import com.example.calitour.model.entity.UserType
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException
import java.util.UUID

class AuthViewModel : ViewModel(){

    val authStateLV = MutableLiveData<AuthState>()
    val errorLV = MutableLiveData<ErrorMessage>()

    fun signUpUser(user :User, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseSignUp(user.email, pass)
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
            if(user.photoUri != Uri.parse("")){
                uploadImage(user.photoUri,newUser["id"]!!.toString(), UserType.USER)

            }
            registerUserType(Firebase.auth.currentUser?.uid.toString(), "user")
        }
    }

    fun signUpEntity(entity: Entity, pass:String){
        viewModelScope.launch (Dispatchers.IO) {
            firebaseSignUp(entity.email, pass)
            val newEntity = hashMapOf(
                "name" to entity.name,
                "id" to Firebase.auth.currentUser?.uid,
                "description" to entity.description,
                "email" to entity.email,
                "photoID" to ""
            )
            Firebase.firestore.collection("entities")
                .document(newEntity["id"]!!.toString())
                .set(newEntity).await()
            if(entity.profilePic != Uri.parse("")){
                uploadImage(entity.profilePic,newEntity["id"]!!.toString(), UserType.ENTITY)

            }
            registerUserType(Firebase.auth.currentUser?.uid.toString(), "entity")
        }

    }

    fun registerUserType(id: String, type: String){
        viewModelScope.launch(Dispatchers.IO){
            val userType = hashMapOf(
                "id" to id,
                "role" to type
            )
            Firebase.firestore.collection(("role-users"))
                .document(id)
                .set(userType).await()
        }
    }


    fun firebaseSignUp(email: String, pass:String){

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = Firebase.auth.createUserWithEmailAndPassword(email, pass).await()
                withContext(Dispatchers.Main){ authStateLV.value = AuthState(result.user?.uid, true)}
                Log.e(">>>", "Registrado")
                Firebase.auth.currentUser
            }catch (e: FirebaseAuthInvalidCredentialsException) {
                withContext(Dispatchers.Main){errorLV.value = ErrorMessage(e.message!!)}
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
            } catch (e: FirebaseException){
                withContext(Dispatchers.Main){errorLV.value = ErrorMessage("Verifique sus credenciales")}
                Log.e(">>>", "Error de autenticación")
            }
        }

    }

    fun uploadImage(uri: Uri, id:String, type: UserType){
        viewModelScope.launch (Dispatchers.IO) {
            try {
                val uuid = UUID.randomUUID().toString()
                Firebase.storage.reference
                    .child("profileImages")
                    .child(uuid)
                    .putFile(uri).await()

                when(type){
                    UserType.USER -> {
                        Firebase.firestore.collection("users")
                            .document(id)
                            .update("photoID", uuid )
                            .await()
                    }
                    UserType.ENTITY -> {
                        Firebase.firestore.collection("entities")
                            .document(id)
                            .update("photoID", uuid )
                            .await()
                    }
                }


            }catch (ex:Exception){
                Log.e("<<<<<", ex.message.toString())
            }

        }
    }
}