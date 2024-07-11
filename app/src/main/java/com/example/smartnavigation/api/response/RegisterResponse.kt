package com.example.smartnavigation.api.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("user_id") val userId: String
)
