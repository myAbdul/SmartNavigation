package com.example.smartnavigation.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiInstance {
    private val intercepter = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder().apply {
        this.addInterceptor(intercepter)
    }.build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://smart-navigation-api.libascatering.com.au/")
//        .baseUrl("http://10.0.0.5:8888/smart-navigation-api/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}