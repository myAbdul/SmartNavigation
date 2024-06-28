package com.example.smartnavigation.api.facility

import retrofit2.http.Body
import retrofit2.http.POST

interface FacilityApi {

    @POST("add_facility")
    suspend fun addFacility(@Body request: AddFacilityRequest): AddFacilityResponse

}