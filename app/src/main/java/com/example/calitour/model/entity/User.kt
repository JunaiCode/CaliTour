package com.example.calitour.model.entity

import android.net.Uri


data class User (
    var id: String,
    var birthday: String,
    var email: String,
    var name : String,
    var phoneNumber: String,
    var points : Int,
    var photoUri: Uri
)