package com.example.smartnavigation.api.facility

import com.google.gson.annotations.SerializedName

data class AddFacilityResponse(
    @SerializedName("facility_id") val facilityId: Int
)
