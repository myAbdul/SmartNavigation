package com.example.smartnavigation.api.facility

import com.google.gson.annotations.SerializedName

data class AddFacilityRequest(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    @SerializedName("user_id") val userId: Int
)
