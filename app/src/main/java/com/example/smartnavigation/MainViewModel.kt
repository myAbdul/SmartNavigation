package com.example.smartnavigation

import android.graphics.Bitmap
import android.util.Base64
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
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt

class MainViewModel : ViewModel() {
    var user: User? = null
    var facilityName: String by mutableStateOf("")
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

    suspend fun addFacility(latitude: Double, longitude: Double) {
        withContext(Dispatchers.IO) {
            val request = AddFacilityRequest(
                facilityName,
                latitude,
                longitude,
                facilityImage!!.scaleImage().toByteArray().base64,
                user!!.userId
            )
            facilityApi.addFacility(request)
        }
    }

    private fun Bitmap.scaleImage(): Bitmap {
        val maxImageSize = 512F
        val ratio = (maxImageSize / width).coerceAtMost(maxImageSize / height)
        val width = (ratio * width).roundToInt()
        val height = (ratio * height).roundToInt()
        return try {
            val resultBitmap = Bitmap.createScaledBitmap(
                this, width,
                height, true
            )
            resultBitmap
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            this
        }
    }

    private fun Bitmap.toByteArray(
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        quality: Int = 80
    ): ByteArray = ByteArrayOutputStream().use {
        compress(format, quality, it)
        it.toByteArray()
    }

    private val ByteArray.base64: String get() = Base64.encodeToString(this, Base64.DEFAULT)
}