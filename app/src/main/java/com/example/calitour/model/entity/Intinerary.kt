package com.example.calitour.model.entity

import java.util.UUID

data class Itinerary(
    var id: UUID,
    var dayString: String,
    var events: ArrayList<String>
)



