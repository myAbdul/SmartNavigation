package com.example.smartnavigation.api.response

import com.google.gson.annotations.SerializedName

data class AddCampusEventResponse(
    @SerializedName("campus_event_id") val campusEventId: Int
)
