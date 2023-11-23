package com.example.calitour.model.repository

import com.example.calitour.model.entity.Event
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EventRepository {


    @GET("/events/{id}")
    fun getAllEntityEvents(@Path("id") id: String): Call<Event>
}