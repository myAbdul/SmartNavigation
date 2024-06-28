package com.example.smartnavigation.api.login

import com.example.smartnavigation.model.User
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): User

}