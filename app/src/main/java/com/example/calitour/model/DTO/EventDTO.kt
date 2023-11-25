package com.example.calitour.model.DTO

import java.util.Date
import java.util.UUID

data class EventDTO(
    var id : String,
    var category: String,
    var date: String,
    var description: String,
    var entityId : String,
    var name : String,
    var place: String,
    var reaction: Long,
    var score : Long,
    var state : String
)
