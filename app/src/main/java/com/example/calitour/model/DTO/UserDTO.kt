package com.example.calitour.model.DTO

import android.net.Uri

data class UserDTO (
    var id: String,
    var birthday: String,
    var email: String,
    var name : String,
    var phoneNumber: String,
    var points : Long,
    var photoUri: String
){
    constructor():this("","","","","", 0, "") {
        // No-argument constructor required for deserialization
    }
}