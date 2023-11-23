package com.example.calitour.model.entity

import java.util.UUID

data class Entity (
    val description : String,
    val email : String,
    val id :  UUID,
    val name : String,
    val password : String,
    val profilePic : String
)