package com.example.calitour.model.repository

import com.example.calitour.model.entity.EntityFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await


class EntityRepository {


    suspend fun getEntityProfile(entityId: String): EntityFirestore{
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
        return  userFirestore!!

    }

}