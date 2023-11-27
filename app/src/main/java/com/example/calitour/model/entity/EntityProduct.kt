package com.example.calitour.model.entity

import android.net.Uri

data class EntityProduct (
    val name: String = "",
    val id: String = "",
    val imageUri: Uri = Uri.parse(""),
    val description: String = "",
    val price: Int = 0
)
