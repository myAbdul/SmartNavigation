package com.example.smartnavigation.api.register

import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi {

    @POST("register_user")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

}