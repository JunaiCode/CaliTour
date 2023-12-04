package com.example.calitour.model.entity

import java.util.UUID

data class Question(
    var correctAns: String,
    var id: UUID,
    var options: ArrayList<String>,
    var title: String
)