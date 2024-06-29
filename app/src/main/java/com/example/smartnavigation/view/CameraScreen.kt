package com.example.smartnavigation.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults.shape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.smartnavigation.MainViewModel
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(navController: NavHostController, viewModel: MainViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Register")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        val modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
        val lensFacing = CameraSelector.LENS_FACING_BACK
        val lifecycleOwner = LocalLifecycleOwner.current
        val context = LocalContext.current
        val preview = Preview.Builder().build()
        val imageCapture = remember {
            ImageCapture.Builder().build()
        }
        val previewView = remember {
            PreviewView(context)
        }
        val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        Card(modifier = modifier.padding(innerPadding)) {
            Box(Modifier.fillMaxWidth()) {
                AndroidView(
                    factory = { previewView }, modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .size(100.dp)
                        .clip(shape)
                        .background(Color.White)
                        .align(Alignment.BottomCenter)
                        .clickable(
                            onClick = {
                                captureImage(imageCapture, context, viewModel, navController)
                            }
                        )
                )
            }
        }

        LaunchedEffect(lensFacing) {
            val cameraProvider = context.getCameraProvider()
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview, imageCapture)
            preview.setSurfaceProvider(previewView.surfaceProvider)
        }
    }
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

private fun captureImage(
    imageCapture: ImageCapture,
    context: Context,
    viewModel: MainViewModel,
    navController: NavHostController
) {
    imageCapture.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(imageProxy: ImageProxy) {
                super.onCaptureSuccess(imageProxy)
                val image = imageProxy.toBitmap()
                val matrix = Matrix()
                matrix.postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
                viewModel.facilityImage =
                    Bitmap.createBitmap(image, 0, 0, image.width, image.height, matrix, true)
                navController.popBackStack()
            }

            override fun onError(e: ImageCaptureException) {
                super.onError(e)
                e.printStackTrace()
            }
        }
    )
}