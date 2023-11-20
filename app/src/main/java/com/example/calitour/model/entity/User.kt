package com.example.calitour.model.entity

import android.net.Uri
import com.google.firebase.Timestamp
import java.util.UUID

data class User (
    var id: String,
    var birthday: String,
    var email: String,
    var name : String,
    var phoneNumber: String,
    var points : Long,
    var photoUri: Uri
)

