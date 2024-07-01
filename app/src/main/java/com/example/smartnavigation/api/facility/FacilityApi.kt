package com.example.smartnavigation.api.facility

import com.example.smartnavigation.model.Facility
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FacilityApi {

    @POST("add_facility")
    suspend fun addFacility(@Body request: AddFacilityRequest): AddFacilityResponse

    @GET("get_all_facilities")
    suspend fun getAllFacilities(): List<Facility>

}