package com.example.pic_a_day_frontend.User

data class LoginResponse(
    val message: String,
    val token: String
)

data class LoginRequest(
    val password: String,
    val username: String
)