package com.example.smartnavigation.api.request

import com.google.gson.annotations.SerializedName

data class AddFacilityRequest(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val image: String?,
    @SerializedName("user_id") val userId: Int
)
