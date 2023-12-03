package com.example.calitour.model.entity

import android.net.Uri

data class EntityProduct (
    val name: String = "",
    val id: String = "",
    val imageUri: Uri = Uri.parse(""),
    val description: String = "",
    val price: Int = 0
)

data class EntityProductFirestore(
    var name: String = "",
    var id: String = "",
    var image: String = "",
    var description: String = "",
    var points: Int = 0
)
