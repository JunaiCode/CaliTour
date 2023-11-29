package com.example.calitour.model.entity

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ItineraryDay(
    val id: String,
    val day: Day,
    val events: ArrayList<String>
) {
    data class Day(
        val nanoseconds: Long,
        val seconds: Long
    )
    fun getFormattedDate(): String {
        val milliseconds = day.seconds * 1000 + day.nanoseconds / 1_000_000
        val dateFormat = SimpleDateFormat("EEE, MMM dd / yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(Date(milliseconds))
        return formattedDate
    }
}



