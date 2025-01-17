package com.example.smartnavigation.api.request

import com.google.gson.annotations.SerializedName

data class AddClassScheduleRequest(
    @SerializedName("department_id") val departmentId: Int,
    @SerializedName("program_name") val programName: String,
    @SerializedName("level_id") val levelId: Int,
    @SerializedName("course_name") val courseName: String,
    val day: String,
    val time: String,
    @SerializedName("user_id") val userId: Int
)
