package com.example.smartnavigation.model

import com.google.gson.annotations.SerializedName

data class College(
    @SerializedName("college_id") val collegeId: Int,
    val name: String
)
