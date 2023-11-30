package com.example.calitour.model.entity

import android.net.Uri
import java.io.Serializable

data class Entity (
    var description : String,
    var email : String,
    var id :  String,
    var name : String,
    var password : String,
    var profilePic : Uri
)

data class EntityFirestore (
    var description: String,
    var email: String,
    var id: String,
    var name: String,
    var photoID: String,
    var facebook: String,
    var x: String,
    var instagram: String
): Serializable{
    constructor():this("","","","","", "", "", "") {
        // No-argument constructor required for deserialization
    }
}