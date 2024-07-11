package com.example.smartnavigation.model

import com.google.gson.annotations.SerializedName

data class Program(
    @SerializedName("program_id") val programId: Int,
    @SerializedName("department_id") val departmentId: Int,
    val name: String
)
