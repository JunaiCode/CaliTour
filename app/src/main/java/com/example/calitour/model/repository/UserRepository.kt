package com.example.calitour.model.repository

import com.example.calitour.model.DTO.UserDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserRepository {


    @GET("/users/userId")
    fun getUserData(@Path("userId")  userId : String): Call<UserDTO>
}