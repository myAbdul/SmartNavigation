package com.example.smartnavigation

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smartnavigation.api.ApiInstance
import com.example.smartnavigation.api.facility.AddFacilityRequest
import com.example.smartnavigation.api.facility.FacilityApi
import com.example.smartnavigation.api.login.LoginApi
import com.example.smartnavigation.api.login.LoginRequest
import com.example.smartnavigation.api.register.RegisterApi
import com.example.smartnavigation.api.register.RegisterRequest
import com.example.smartnavigation.model.Facility
import com.example.smartnavigation.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private val loginApi = ApiInstance.createService(LoginApi::class.java)
    private val registerApi = ApiInstance.createService(RegisterApi::class.java)
    private val facilityApi = ApiInstance.createService(FacilityApi::class.java)

    var user: User? = null
    var facilityName: String by mutableStateOf("")
    var facilityImage: Bitmap? by mutableStateOf(null)
    var facilityList: List<Facility> by mutableStateOf(listOf())

    suspend fun login(request: LoginRequest) {
        withContext(Dispatchers.IO) {
            user = loginApi.login(request)
        }
    }

    suspend fun register(request: RegisterRequest) {
        withContext(Dispatchers.IO) {
            registerApi.register(request)
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
            facilityApi.addFacility(request)
        }
    }

    suspend fun getAllFacilities() {
        withContext(Dispatchers.IO) {
            try {
                facilityList = facilityApi.getAllFacilities()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}