package com.example.calitour.model.repository

import com.example.calitour.model.entity.Entity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EntityRepository {



    @GET("/entity/{id}.json")
    fun getEntityById( @Path("id") id: String) : Call<Entity>
}