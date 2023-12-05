package com.example.calitour.model.DTO

import java.util.UUID

data class EntityDTO (
    val description : String,
    val email : String,
    val id : String,
    val name : String,
    val password : String,
    val profilePic : String
){
    constructor(): this("","","", "","",""){

    }
}