package com.example.calitour.model.services

import com.example.calitour.model.repository.EntityRepository
import com.example.calitour.model.repository.EventRepository
import com.example.calitour.model.repository.ItineraryRepository
import com.example.calitour.model.repository.UserRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitServices {


    var entityService = Retrofit.Builder()
        .baseUrl("base")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var eventService = Retrofit.Builder()
        .baseUrl("base")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var userService = Retrofit.Builder()
        .baseUrl("base")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var itineraryService = Retrofit.Builder()
        .baseUrl("base")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var entityRepository  = entityService.create(EntityRepository::class.java)
    var eventRepository = eventService.create(EventRepository::class.java)
    var userRepository = userService.create(UserRepository::class.java)
    var itineraryRepository = itineraryService.create(ItineraryRepository::class.java)
}