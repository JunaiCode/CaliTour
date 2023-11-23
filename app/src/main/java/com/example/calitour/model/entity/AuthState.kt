package com.example.calitour.model.entity

data class AuthState (
    var userID: String? = null,
    var isAuth: Boolean
)

data class ErrorMessage(
    var message: String
)