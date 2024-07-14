package com.example.smartnavigation.api.request

import com.google.gson.annotations.SerializedName

data class AddCampusEventRequest(
    val name: String,
    @SerializedName("date") val date: String,
    @SerializedName("time") val time: String,
    @SerializedName("facility_id") val facilityId: Int,
    @SerializedName("user_id") val userId: Int
)
