package com.example.calitour.model.DTO


data class ItineraryDTO (
    val id : String = "",
    val day : String = "",
    val events :ArrayList<String> =ArrayList()
)