package com.example.calitour.model.entity

import java.util.Date
import java.util.UUID

data class Event (
    var id : UUID,
    var category: String,
    var date: Date,
    var description: String,
    var entityId : UUID,
    var name : String,
    var place: String,
    var reaction: Int,
    var score : Float,
    var state : String
)