package com.example.smartnavigation.model

import com.google.gson.annotations.SerializedName

data class Level(
    @SerializedName("level_id") val levelId: Int,
    val name: String
)
