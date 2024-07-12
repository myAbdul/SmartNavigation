package com.example.smartnavigation.api

import com.example.smartnavigation.api.request.AddClassScheduleRequest
import com.example.smartnavigation.api.request.AddFacilityRequest
import com.example.smartnavigation.api.request.LoginRequest
import com.example.smartnavigation.api.request.RegisterRequest
import com.example.smartnavigation.api.response.AddClassScheduleResponse
import com.example.smartnavigation.api.response.AddFacilityResponse
import com.example.smartnavigation.api.response.GetClassScheduleFormDataResponse
import com.example.smartnavigation.api.response.RegisterResponse
import com.example.smartnavigation.model.ClassSchedule
import com.example.smartnavigation.model.Facility
import com.example.smartnavigation.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SmartNavigationApi {

    @POST("register_user")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("login")
    suspend fun login(@Body request: LoginRequest): User

    @POST("add_facility")
    suspend fun addFacility(@Body request: AddFacilityRequest): AddFacilityResponse

    @GET("get_all_facilities")
    suspend fun getAllFacilities(): List<Facility>

    @GET("get_class_schedules/{program_id}")
    suspend fun getClassSchedules(@Path("program_id") programId: Int): List<ClassSchedule>

    @GET("get_class_schedule_form_data")
    suspend fun getClassScheduleFormData(): GetClassScheduleFormDataResponse

    @POST("add_class_schedule")
    suspend fun addClassSchedule(@Body request: AddClassScheduleRequest): AddClassScheduleResponse

}