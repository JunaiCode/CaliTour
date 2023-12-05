package com.example.calitour.model.repository

import com.example.calitour.model.entity.EntityProductFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class ProductRepository {

    suspend fun getProductsByEntityId(entityId: String): List<EntityProductFirestore>{
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

        return productsFirestore
    }
}