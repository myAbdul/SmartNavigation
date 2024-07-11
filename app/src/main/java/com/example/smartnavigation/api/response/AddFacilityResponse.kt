package com.example.smartnavigation.api.response

import com.google.gson.annotations.SerializedName

data class AddFacilityResponse(
    @SerializedName("facility_id") val facilityId: Int
)
