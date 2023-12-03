package com.example.calitour.model.DTO

import com.google.firebase.Timestamp


data class EventDocumentDTO(
    var category: String = "",
    var date: Timestamp = Timestamp.now() ,
    var description: String = "",
    var entityId: String = "",
    var id: String = "",
    var img: String = "",
    var name: String = "",
    var place: String = "",
    var reaction: Int = 0,
    var score: Double = 0.0,
    var state: String = ""
)
data class EventFullDTO (
    var category: String = "",
    var date: Timestamp = Timestamp.now() ,
    var description: String = "",
    var entityId: String = "",
    var id: String = "",
    var img: String = "",
    var name: String = "",
    var place: String = "",
    var reaction: Int = 0,
    var score: Double = 0.0,
    var state: String = "",
    var entityName: String = ""
)
