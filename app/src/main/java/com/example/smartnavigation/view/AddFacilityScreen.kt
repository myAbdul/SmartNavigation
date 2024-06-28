package com.example.smartnavigation.view

import android.Manifest
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.smartnavigation.MainViewModel
import com.example.smartnavigation.api.facility.AddFacilityRequest
import com.example.smartnavigation.theme.SmartNavigationTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddFacilityScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Add Facility")
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
        var name by remember { mutableStateOf("") }
        var loading by remember { mutableStateOf(false) }
        var loadingText by remember { mutableStateOf("") }
        val coroutineScope = rememberCoroutineScope()
        val permissionState = rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )
        val context = LocalContext.current
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        Column(
            modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (permissionState.allPermissionsGranted) {
                OutlinedTextField(
                    modifier = modifier,
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                )
                Button(
                    modifier = modifier,
                    onClick = {
                        if (!loading) {
                            if (name.isNotBlank()) {
                                loadingText = "Picking location..."
                                loading = true
                                fusedLocationProviderClient.getCurrentLocation(
                                    Priority.PRIORITY_HIGH_ACCURACY,
                                    CancellationTokenSource().token,
                                ).addOnSuccessListener { location ->
                                    loadingText = "Adding facility..."
                                    coroutineScope.launch {
                                        try {
                                            viewModel.addFacility(
                                                AddFacilityRequest(
                                                    name,
                                                    location.latitude,
                                                    location.longitude,
                                                    viewModel.user!!.userId
                                                )
                                            )
                                            Toast.makeText(
                                                context,
                                                "Facility added successfully!",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            navController.popBackStack()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }
                                }.addOnFailureListener { exception ->

                                }
                            }
                        }
                    },
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (loading) {
                            Text(loadingText)
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .width(24.dp)
                                    .height(24.dp),
                                color = MaterialTheme.colorScheme.secondary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            )
                        } else {
                            Text("Register")
                        }
                    }
                }
            } else {
                Text(
                    text = "Location permission required for this feature to be available.\nPlease grant the permission.",
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = { permissionState.launchMultiplePermissionRequest() }) {
                    Text(text = "Request permission")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddFacilityPreview() {
    SmartNavigationTheme {
        AddFacilityScreen(rememberNavController(), MainViewModel())
    }
}