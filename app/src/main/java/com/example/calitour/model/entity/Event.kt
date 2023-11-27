package com.example.calitour.model.entity

import java.util.UUID

data class Event(
    var id: UUID,
    var category: String,
    var date: String,
    var description: String,
    var entityId: UUID,
    var name: String,
    var place: String,
    var reaction: Int,
    var score: Double,
    var state: String,
    var img: String,
    var prices: ArrayList<Price>,
    var badges: ArrayList<Badge>,
    var trivias: ArrayList<Trivia>
)