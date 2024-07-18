package com.example.smartnavigation.api.response

import com.example.smartnavigation.model.College
import com.example.smartnavigation.model.Department
import com.example.smartnavigation.model.Level
import com.example.smartnavigation.model.Program
import com.google.gson.annotations.SerializedName

data class GetClassScheduleFormDataResponse(
    @SerializedName("all_colleges") val allColleges: List<College>,
    @SerializedName("all_departments") val allDepartments: List<Department>,
    @SerializedName("all_programs") val allPrograms: List<Program>,
    @SerializedName("all_levels") val allLevels: List<Level>
)
