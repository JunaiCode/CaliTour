package com.example.calitour.model.DTO

import com.google.firebase.Timestamp
import java.util.Date
import java.util.UUID

data class EventDTO(
    var id : String,
    var category: String,
    var date: Timestamp,
    var description: String,
    var entityId : String,
    var name : String,
    var place: String,
    var reaction: Long,
    var score : Long,
    var state : String,
    var img : String
){
    constructor(): this("","",Timestamp.now(),"","","","",0,0,"",""){

    }
}
