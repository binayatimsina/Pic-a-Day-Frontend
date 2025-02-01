package com.example.pic_a_day_frontend.User

data class SavedUserDataResponse(
    val `data`: Data,
    val message: String
)

data class Data(
    val imageURL: String,
    val note: String,
    val username: String,
    val date: String
)