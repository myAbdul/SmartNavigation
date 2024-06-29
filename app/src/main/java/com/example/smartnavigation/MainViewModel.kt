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
import com.example.smartnavigation.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    var user: User? = null
    var facilityImage: Bitmap? by mutableStateOf(null)
    private val loginApi = ApiInstance.createService(LoginApi::class.java)
    private val registerApi = ApiInstance.createService(RegisterApi::class.java)
    private val facilityApi = ApiInstance.createService(FacilityApi::class.java)

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

    suspend fun addFacility(request: AddFacilityRequest) {
        withContext(Dispatchers.IO) {
            facilityApi.addFacility(request)
        }
    }
}