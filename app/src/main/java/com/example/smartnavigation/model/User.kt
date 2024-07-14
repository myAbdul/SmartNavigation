package com.example.smartnavigation.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id") val userId: Int,
    val username: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String
)
