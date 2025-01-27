package com.example.pic_a_day_frontend.data

import com.example.pic_a_day_frontend.User.LoginRequest
import com.example.pic_a_day_frontend.User.LoginResponse
import com.example.pic_a_day_frontend.User.NewUserInfo
import com.example.pic_a_day_frontend.User.NewUserResponse
import com.example.pic_a_day_frontend.User.UsersList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @GET("getUsers/")
    suspend fun getUsers():Response<UsersList>

    @POST("create/")
    suspend fun createUser(@Body body: NewUserInfo):Response<NewUserResponse>

    @POST("login")
    suspend fun loginUser(@Body body: LoginRequest):Response<LoginResponse>
}