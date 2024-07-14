package com.example.smartnavigation.model

import com.google.gson.annotations.SerializedName

data class CampusEvent(
    @SerializedName("campus_event_id") val campusEventId: Int,
    val name: String,
    @SerializedName("date") val date: String,
    @SerializedName("time") val time: String,
    @SerializedName("facility_name") val facilityName: String,
    @SerializedName("facility_latitude") val facilityLatitude: Double,
    @SerializedName("facility_longitude") val facilityLongitude: Double
)
