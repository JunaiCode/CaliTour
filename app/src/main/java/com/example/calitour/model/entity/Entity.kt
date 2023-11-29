package com.example.calitour.model.entity

import android.net.Uri

data class Entity (
    var description : String,
    var email : String,
    var id :  String,
    var name : String,
    var password : String,
    var profilePic : Uri
)