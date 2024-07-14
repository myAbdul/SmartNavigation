package com.example.smartnavigation.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.smartnavigation.MainViewModel
import com.example.smartnavigation.api.request.AddCampusEventRequest
import com.example.smartnavigation.model.Facility
import com.example.smartnavigation.theme.SmartNavigationTheme
import com.example.smartnavigation.theme.defaultPadding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCampusEventScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Add Campus Event")
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
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val emptyFacility = Facility(0, "", 0.0, 0.0, "")
        var facilityIsExpanded by remember { mutableStateOf(false) }
        var selectedFacility by remember { mutableStateOf(emptyFacility) }
        var name by remember { mutableStateOf("") }
        var date by remember { mutableStateOf("") }
        var time by remember { mutableStateOf("") }
        val timeState = rememberTimePickerState()
        val dateState = rememberDatePickerState()
        var displayDateDialog by remember { mutableStateOf(false) }
        var displayTimeDialog by remember { mutableStateOf(false) }
        var showErrorDialog by remember { mutableStateOf(false) }

        Column(
            modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = modifier,
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
            )
            ExposedDropdownMenuBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(defaultPadding),
                expanded = facilityIsExpanded,
                onExpandedChange = { facilityIsExpanded = it },
            ) {
                OutlinedTextField(
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = selectedFacility.name,
                    onValueChange = { },
                    label = { Text("Venue") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = facilityIsExpanded
                        )
                    },
                )
                ExposedDropdownMenu(modifier = Modifier.fillMaxWidth(),
                    expanded = facilityIsExpanded,
                    onDismissRequest = {
                        facilityIsExpanded = false
                    }) {
                    viewModel.facilityList.forEach { option ->
                        DropdownMenuItem(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                selectedFacility = option
                                facilityIsExpanded = false
                            },
                            text = {
                                Text(text = option.name)
                            },
                        )
                    }
                }
            }
            OutlinedTextField(
                modifier = modifier,
                readOnly = true,
                interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Release) {
                                displayDateDialog = true
                            }
                        }
                    }
                },
                value = date,
                onValueChange = { },
                label = { Text("Date") },
            )
            OutlinedTextField(
                modifier = modifier,
                readOnly = true,
                interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Release) {
                                displayTimeDialog = true
                            }
                        }
                    }
                },
                value = time,
                onValueChange = { },
                label = { Text("Time") },
            )
            Button(
                modifier = modifier,
                onClick = {
                    if (!viewModel.loading) {
                        coroutineScope.launch {
                            val result = viewModel.addCampusEvent(
                                AddCampusEventRequest(
                                    name,
                                    date,
                                    time,
                                    selectedFacility.facilityId,
                                    viewModel.user?.userId ?: 0
                                )
                            )
                            if (result) {
                                Toast.makeText(
                                    context, "Campus event added successfully!", Toast.LENGTH_LONG
                                ).show()
                                navController.popBackStack()
                            } else {
                                showErrorDialog = true
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
                    if (viewModel.loading) {
                        Text("Adding campus event...", modifier = Modifier.padding(4.dp))
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .width(24.dp)
                                .height(24.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    } else {
                        Text("Add", modifier = Modifier.padding(4.dp))
                    }
                }
            }

            if (displayDateDialog) {
                DatePickerDialog(onDismissRequest = {
                    displayDateDialog = false
                }, confirmButton = {
                    TextButton(
                        onClick = {
                            displayDateDialog = false
                            date =
                                dateState.selectedDateMillis?.let { convertMillisToDate(it) } ?: ""
                        },
                    ) {
                        Text("OK")
                    }
                }, dismissButton = {
                    TextButton(onClick = { displayDateDialog = false }) { Text("Cancel") }
                }) {
                    DatePicker(state = dateState)
                }
            }

            if (displayTimeDialog) {
                Dialog(onDismissRequest = { displayTimeDialog = false }) {
                    Card(
                        modifier = Modifier.padding(defaultPadding),
                    ) {
                        Column(
                            modifier = Modifier,
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            TimePicker(
                                state = timeState, modifier = Modifier.padding(defaultPadding)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(defaultPadding),
                                horizontalArrangement = Arrangement.End,
                            ) {
                                TextButton(
                                    onClick = {
                                        displayTimeDialog = false
                                    },
                                ) {
                                    Text("Cancel")
                                }
                                TextButton(
                                    onClick = {
                                        time = "${timeState.hour}:${timeState.minute}"
                                        displayTimeDialog = false
                                    },
                                    modifier = Modifier.padding(start = 8.dp),
                                ) {
                                    Text("Confirm")
                                }
                            }
                        }
                    }
                }
            }

        }

        if (showErrorDialog) {
            AlertDialog(title = {
                Text(text = "Error")
            }, text = {
                Text(text = viewModel.errorMessage)
            }, onDismissRequest = {
                showErrorDialog = false
            }, confirmButton = {
                TextButton(onClick = {
                    showErrorDialog = false
                }) {
                    Text("Confirm")
                }
            })
        }
    }

    LaunchedEffect(true) {
        viewModel.getAllFacilities()
    }
}

@SuppressLint("SimpleDateFormat")
private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(Date(millis))
}

@Preview(showBackground = true)
@Composable
fun AddCampusEventPreview() {
    SmartNavigationTheme {
        AddCampusEventScreen(rememberNavController(), MainViewModel())
    }
}