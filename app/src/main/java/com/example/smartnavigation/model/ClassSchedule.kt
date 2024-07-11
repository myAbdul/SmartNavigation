package com.example.smartnavigation.model

import com.google.gson.annotations.SerializedName

data class ClassSchedule(
    @SerializedName("class_schedule_id") val classScheduleId: Int,
    @SerializedName("program_id") val programId: Int,
    @SerializedName("course_name") val courseName: String,
    val day: String,
    val time: String
)
