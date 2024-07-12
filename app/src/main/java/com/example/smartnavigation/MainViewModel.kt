package com.example.smartnavigation

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smartnavigation.api.ApiInstance
import com.example.smartnavigation.api.SmartNavigationApi
import com.example.smartnavigation.api.request.AddClassScheduleRequest
import com.example.smartnavigation.api.request.AddFacilityRequest
import com.example.smartnavigation.api.request.LoginRequest
import com.example.smartnavigation.api.request.RegisterRequest
import com.example.smartnavigation.model.ClassSchedule
import com.example.smartnavigation.model.College
import com.example.smartnavigation.model.Department
import com.example.smartnavigation.model.Facility
import com.example.smartnavigation.model.Program
import com.example.smartnavigation.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private val smartNavigationApi = ApiInstance.createService(SmartNavigationApi::class.java)

    private var user: User? = null
    var facilityName: String by mutableStateOf("")
    var facilityImage: Bitmap? by mutableStateOf(null)
    var facilityList: List<Facility> by mutableStateOf(listOf())
    var classScheduleList: List<ClassSchedule> by mutableStateOf(listOf())
    var programList: List<Program> by mutableStateOf(listOf())
    var collegeList: List<College> by mutableStateOf(listOf())
    var departmentList: List<Department> by mutableStateOf(listOf())

    suspend fun login(request: LoginRequest) {
        withContext(Dispatchers.IO) {
            user = smartNavigationApi.login(request)
        }
    }

    suspend fun register(request: RegisterRequest) {
        withContext(Dispatchers.IO) {
            smartNavigationApi.register(request)
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

    suspend fun getClassSchedules(programId: Int) {
        withContext(Dispatchers.IO) {
            try {
                classScheduleList = smartNavigationApi.getClassSchedules(programId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getClassScheduleFormData() {
        withContext(Dispatchers.IO) {
            try {
                val response = smartNavigationApi.getClassScheduleFormData()
                programList = response.allPrograms
                collegeList = response.allColleges
                departmentList = response.allDepartments
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
}