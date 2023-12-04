package com.example.calitour.model.DTO

import android.net.Uri

data class UserDTO (
    var id: String,
    var birthday: String,
    var email: String,
    var name : String,
    var phoneNumber: String,
    var points : Long,
    var photoID: String
){
    constructor():this("","","","","", 0, "") {
        // No-argument constructor required for deserialization
    }

    override fun toString(): String {
        return "id : ${this.id}\n" +
                "birthday : ${this.birthday}\n" +
                "email : ${this.email}\n"+
                "name : ${this.email}\n"+
                "phoneNumber : ${this.phoneNumber}\n"+
                "points : ${this.points}\n"+
                "photoURI : ${this.photoID}"


    }
}