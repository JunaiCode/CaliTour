package com.example.calitour.model.DTO

data class ReviewDTO(
    val comment : String,
    val id : String,
    val idEvent: String,
    val idUser : String,
    val score : Long
)