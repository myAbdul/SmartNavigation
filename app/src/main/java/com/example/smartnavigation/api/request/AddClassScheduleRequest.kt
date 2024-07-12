package com.example.smartnavigation.api.request

import com.google.gson.annotations.SerializedName

data class AddClassScheduleRequest(
    @SerializedName("department_id") val departmentId: Int,
    @SerializedName("program_name") val programName: String,
    @SerializedName("course_name") val courseName: String,
    val day: String,
    val time: String
)
