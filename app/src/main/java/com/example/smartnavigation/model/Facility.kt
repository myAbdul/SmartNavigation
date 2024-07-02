package com.example.smartnavigation.model

import com.google.gson.annotations.SerializedName

data class Facility(
    @SerializedName("facility_id") val facilityId: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val image: String?
)
