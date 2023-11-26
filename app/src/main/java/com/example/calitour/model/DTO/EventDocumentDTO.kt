package com.example.calitour.model.DTO

import java.util.UUID

data class EventDocumentDTO(
    var id: String,
    var category: String,
    var date: String,
    var description: String,
    var entityId: String,
    var name: String,
    var place: String,
    var reaction: Int,
    var score: Double,
    var state: String,
    var img: String,
)
