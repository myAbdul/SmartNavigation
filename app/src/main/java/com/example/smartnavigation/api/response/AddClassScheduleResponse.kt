package com.example.smartnavigation.api.response

import com.google.gson.annotations.SerializedName

data class AddClassScheduleResponse(
    @SerializedName("class_schedule_id") val classScheduleId: Int
)
