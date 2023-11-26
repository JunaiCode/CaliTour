package com.example.calitour.model.entity

import java.util.UUID

data class Trivia(
    var id: UUID,
    var state: String,
    var title: String,
    var questions: ArrayList<Question>
)