package com.example.calitour.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calitour.model.entity.EntityFirestore
import com.example.calitour.model.entity.EntityProduct
import com.example.calitour.model.entity.EntityProductFirestore
import com.example.calitour.model.entity.UserType
import com.google.firebase.auth.ktx.auth
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
    var profile =MutableLiveData<EntityFirestore>()
    var products = MutableLiveData<List<EntityProductFirestore>>()

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

    fun loadProducts(){
        viewModelScope.launch(Dispatchers.IO) {
            val entityId = Firebase.auth.currentUser?.uid.toString()
            val snapshot = Firebase.firestore.collection("entities")
                .document(entityId)
                .collection("products")
                .get().await()

            val productsFirestore = snapshot.toObjects(EntityProductFirestore::class.java)

            for (product in productsFirestore) {
                if (product.image!=""){
                    val imageUrl = Firebase.storage.reference
                        .child("productImages")
                        .child(product.image)
                        .downloadUrl
                        .await()
                        .toString()

                    product.image=imageUrl
                }
            }

            withContext(Dispatchers.Main){
                products.value=productsFirestore
            }
        }
    }

     fun loadProfile(){
        viewModelScope.launch (Dispatchers.IO) {
            val entityId = Firebase.auth.currentUser?.uid.toString()
            var userFirestore: EntityFirestore? = null
            val snapshot = Firebase.firestore.collection("entities")
                .document(entityId)
                .get().await()

            if (snapshot != null) {
                userFirestore = snapshot.toObject(EntityFirestore::class.java)
                if (userFirestore?.photoID != "") {
                    val imageUrl = Firebase.storage.reference.child("profileImages")
                        .child(userFirestore!!.photoID)
                        .downloadUrl.await().toString()
                    userFirestore.photoID= imageUrl
                }
            }

            withContext(Dispatchers.Main) {
                profile.value = userFirestore!!
                Log.e("<<<", profile.value.toString())
            }
        }



    }

    fun updateEntity(entity: EntityFirestore){
        viewModelScope.launch (Dispatchers.IO) {
            val snapshot = Firebase.firestore.collection("entities")
                .document(entity.id)
                .get().await().toObject(EntityFirestore::class.java)
            if(snapshot!=null){
                val newEntity = hashMapOf(
                    "name" to entity.name,
                    "id" to entity.id,
                    "description" to entity.description,
                    "email" to entity.email,
                    "photoID" to snapshot.photoID,
                    "facebook" to entity.facebook,
                    "X" to entity.x,
                    "instagram" to entity.instagram
                ) as MutableMap<String?,Any>

                Firebase.firestore.collection("entities")
                    .document(entity.id)
                    .update(newEntity).await()

                if(entity.photoID!=""){
                    if(newEntity["photoID"].toString()!=""){
                        updateImage(Uri.parse(entity.photoID), newEntity["photoID"].toString())
                    }else {
                        uploadImage(Uri.parse(entity.photoID), entity.id, UserType.ENTITY)
                    }
                }

                loadProfile()
            }
        }
    }

    fun updateImage(uri: Uri, imageId: String){

        viewModelScope.launch (Dispatchers.IO) {
            Firebase.storage.reference
                .child("profileImages")
                .child(imageId)
                .putFile(uri).await()
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