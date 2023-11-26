package com.example.calitour.model.entity

import java.util.UUID

data class Price(
    var description: String,
    var fee: Double,
    var id: UUID,
    var name: String
)