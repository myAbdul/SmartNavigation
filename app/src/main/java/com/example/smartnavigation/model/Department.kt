package com.example.smartnavigation.model

import com.google.gson.annotations.SerializedName

data class Department(
    @SerializedName("department_id") val departmentId: Int,
    @SerializedName("college_id") val collegeId: Int,
    val name: String
)
