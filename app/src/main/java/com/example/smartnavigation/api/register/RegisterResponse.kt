package com.example.smartnavigation.api.register

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("user_id") val userId: String
)
