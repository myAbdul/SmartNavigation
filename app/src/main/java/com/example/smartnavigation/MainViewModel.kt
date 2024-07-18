package com.example.smartnavigation

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smartnavigation.api.ApiInstance
import com.example.smartnavigation.api.SmartNavigationApi
import com.example.smartnavigation.api.request.AddCampusEventRequest
import com.example.smartnavigation.api.request.AddClassScheduleRequest
import com.example.smartnavigation.api.request.AddFacilityRequest
import com.example.smartnavigation.api.request.LoginRequest
import com.example.smartnavigation.api.request.RegisterRequest
import com.example.smartnavigation.model.CampusEvent
import com.example.smartnavigation.model.ClassSchedule
import com.example.smartnavigation.model.College
import com.example.smartnavigation.model.Department
import com.example.smartnavigation.model.Facility
import com.example.smartnavigation.model.Level
import com.example.smartnavigation.model.Program
import com.example.smartnavigation.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private val smartNavigationApi = ApiInstance.createService(SmartNavigationApi::class.java)

    var user: User? = null
    var facilityName: String by mutableStateOf("")
    var facilityImage: Bitmap? by mutableStateOf(null)
    var facilityList: List<Facility> by mutableStateOf(listOf())
    var classScheduleList: List<ClassSchedule> by mutableStateOf(listOf())
    var programList: List<Program> by mutableStateOf(listOf())
    var collegeList: List<College> by mutableStateOf(listOf())
    var departmentList: List<Department> by mutableStateOf(listOf())
    var levelList: List<Level> by mutableStateOf(listOf())
    var loading by mutableStateOf(false)
    var campusEventList: List<CampusEvent> by mutableStateOf(listOf())
    var errorMessage: String by mutableStateOf("")

    suspend fun login(request: LoginRequest): Boolean {
        return withContext(Dispatchers.IO) {
            loading = true
            try {
                user = smartNavigationApi.login(request)
                loading = false
                true
            } catch (e: Exception) {
                errorMessage = e.message ?: "Error"
                loading = false
                false
            }
        }
    }

    suspend fun register(
        firstName: String,
        lastName: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            if (validateRegisterInputs(
                    firstName,
                    lastName,
                    username,
                    password,
                    confirmPassword
                )
            ) {
                loading = true
                try {
                    smartNavigationApi.register(
                        RegisterRequest(
                            firstName,
                            lastName,
                            username,
                            password
                        )
                    )
                    loading = false
                    true
                } catch (e: Exception) {
                    errorMessage = e.message ?: "Error"
                    loading = false
                    false
                }
            } else {
                false
            }
        }
    }

    suspend fun addFacility(latitude: Double, longitude: Double) {
        withContext(Dispatchers.IO) {
            val request = AddFacilityRequest(
                facilityName,
                latitude,
                longitude,
                facilityImage?.let { decodeImage(it) },
                user!!.userId
            )
            smartNavigationApi.addFacility(request)
        }
    }

    suspend fun getAllFacilities() {
        withContext(Dispatchers.IO) {
            try {
                facilityList = smartNavigationApi.getAllFacilities()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getClassSchedules(programId: Int, levelId: Int) {
        withContext(Dispatchers.IO) {
            loading = true
            try {
                classScheduleList = smartNavigationApi.getClassSchedules(
                    mapOf(
                        "program_id" to programId,
                        "level_id" to levelId
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            loading = false
        }
    }

    suspend fun getClassScheduleFormData() {
        withContext(Dispatchers.IO) {
            try {
                val response = smartNavigationApi.getClassScheduleFormData()
                programList = response.allPrograms
                collegeList = response.allColleges
                departmentList = response.allDepartments
                levelList = response.allLevels
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun addClassSchedule(request: AddClassScheduleRequest) {
        withContext(Dispatchers.IO) {
            smartNavigationApi.addClassSchedule(request)
        }
    }

    suspend fun getCampusEvents() {
        withContext(Dispatchers.IO) {
            loading = true
            try {
                campusEventList = smartNavigationApi.getCampusEvents()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            loading = false
        }
    }

    suspend fun addCampusEvent(request: AddCampusEventRequest): Boolean {
        return withContext(Dispatchers.IO) {
            if (validateCampusEventRequest(request)) {
                loading = true
                try {
                    smartNavigationApi.addCampusEvent(request)
                    loading = false
                    true
                } catch (e: Exception) {
                    errorMessage = e.message ?: "Error"
                    loading = false
                    false
                }
            } else {
                false
            }
        }
    }

    private fun validateRegisterInputs(
        firstName: String,
        lastName: String,
        username: String,
        password: String,
        confirmPassword: String,
    ): Boolean {
        return if (firstName.isBlank()) {
            errorMessage = "First name cannot be empty"
            false
        } else if (lastName.isBlank()) {
            errorMessage = "Last name cannot be empty"
            false
        } else if (username.isBlank()) {
            errorMessage = "Username cannot be empty"
            false
        } else if (password.isBlank()) {
            errorMessage = "Password cannot be empty"
            false
        } else if (password != confirmPassword) {
            errorMessage = "Passwords do not match"
            false
        } else {
            true
        }
    }

    private fun validateCampusEventRequest(request: AddCampusEventRequest): Boolean {
        return if (request.name.isBlank() || request.date.isBlank() || request.time.isBlank() || request.facilityId < 1) {
            errorMessage = "Complete all fields!"
            false
        } else {
            true
        }
    }
}