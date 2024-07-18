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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.smartnavigation.api.request.AddClassScheduleRequest
import com.example.smartnavigation.model.College
import com.example.smartnavigation.model.Department
import com.example.smartnavigation.model.Level
import com.example.smartnavigation.theme.SmartNavigationTheme
import com.example.smartnavigation.theme.defaultPadding
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddClassScheduleScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Add Class Schedule")
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
        var loading by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        val emptyDepartment = Department(0, 0, "")
        val emptyCollege = College(0, "")
        val emptyLevel = Level(0, "")
        var collegeIsExpanded by remember { mutableStateOf(false) }
        var selectedCollege by remember { mutableStateOf(emptyCollege) }
        var departmentIsExpanded by remember { mutableStateOf(false) }
        var selectedDepartment by remember { mutableStateOf(emptyDepartment) }
        var programIsExpanded by remember { mutableStateOf(false) }
        var selectedProgram by remember { mutableStateOf("") }
        var levelIsExpanded by remember { mutableStateOf(false) }
        var selectedLevel by remember { mutableStateOf(emptyLevel) }
        var dayIsExpanded by remember { mutableStateOf(false) }
        var selectedDay by remember { mutableStateOf("") }
        var courseName by remember { mutableStateOf("") }
        var time by remember { mutableStateOf("") }
        val timeState = rememberTimePickerState()
        var displayTimeDialog by remember { mutableStateOf(false) }
        val dayList =
            listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

        Column(
            modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ExposedDropdownMenuBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(defaultPadding),
                expanded = collegeIsExpanded,
                onExpandedChange = { collegeIsExpanded = it },
            ) {
                OutlinedTextField(
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = selectedCollege.name,
                    onValueChange = { },
                    label = { Text("School") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = collegeIsExpanded
                        )
                    },
                )
                ExposedDropdownMenu(modifier = Modifier.fillMaxWidth(),
                    expanded = collegeIsExpanded,
                    onDismissRequest = {
                        collegeIsExpanded = false
                    }) {
                    viewModel.collegeList.forEach { option ->
                        DropdownMenuItem(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                selectedCollege = option
                                selectedDepartment =
                                    viewModel.departmentList.firstOrNull { it.collegeId == selectedCollege.collegeId }
                                        ?: emptyDepartment
                                selectedProgram =
                                    viewModel.programList.firstOrNull { it.departmentId == selectedDepartment.departmentId }?.name
                                        ?: ""
                                collegeIsExpanded = false
                            },
                            text = {
                                Text(text = option.name)
                            },
                        )
                    }
                }
            }
            ExposedDropdownMenuBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(defaultPadding),
                expanded = departmentIsExpanded,
                onExpandedChange = { departmentIsExpanded = it },
            ) {
                OutlinedTextField(
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = selectedDepartment.name,
                    onValueChange = { },
                    label = { Text("Department") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = departmentIsExpanded
                        )
                    },
                )
                ExposedDropdownMenu(modifier = Modifier.fillMaxWidth(),
                    expanded = departmentIsExpanded,
                    onDismissRequest = {
                        departmentIsExpanded = false
                    }) {
                    viewModel.departmentList.filter { it.collegeId == selectedCollege.collegeId }
                        .forEach { option ->
                            DropdownMenuItem(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    selectedDepartment = option
                                    selectedProgram =
                                        viewModel.programList.firstOrNull { it.departmentId == selectedDepartment.departmentId }?.name
                                            ?: ""
                                    departmentIsExpanded = false
                                },
                                text = {
                                    Text(text = option.name)
                                },
                            )
                        }
                }
            }
            ExposedDropdownMenuBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(defaultPadding),
                expanded = programIsExpanded,
                onExpandedChange = { programIsExpanded = it },
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = selectedProgram,
                    onValueChange = {
                        selectedProgram = it
                        programIsExpanded = false
                    },
                    label = { Text("Program") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = programIsExpanded
                        )
                    },
                )
                ExposedDropdownMenu(modifier = Modifier.fillMaxWidth(),
                    expanded = programIsExpanded,
                    onDismissRequest = {
                        programIsExpanded = false
                    }) {
                    viewModel.programList.filter { it.departmentId == selectedDepartment.departmentId }
                        .forEach { option ->
                            DropdownMenuItem(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    selectedProgram = option.name
                                    programIsExpanded = false
                                },
                                text = {
                                    Text(text = option.name)
                                },
                            )
                        }
                }
            }
            ExposedDropdownMenuBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(defaultPadding),
                expanded = levelIsExpanded,
                onExpandedChange = { levelIsExpanded = it },
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    value = selectedLevel.name,
                    onValueChange = {},
                    label = { Text("Level") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = levelIsExpanded
                        )
                    },
                )
                ExposedDropdownMenu(modifier = Modifier.fillMaxWidth(),
                    expanded = levelIsExpanded,
                    onDismissRequest = {
                        levelIsExpanded = false
                    }) {
                    viewModel.levelList.forEach { option ->
                        DropdownMenuItem(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                selectedLevel = option
                                levelIsExpanded = false
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
                value = courseName,
                onValueChange = { courseName = it },
                label = { Text("Course Name") },
            )
            ExposedDropdownMenuBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(defaultPadding),
                expanded = dayIsExpanded,
                onExpandedChange = { dayIsExpanded = it },
            ) {
                OutlinedTextField(
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = selectedDay,
                    onValueChange = { },
                    label = { Text("Day") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = dayIsExpanded
                        )
                    },
                )
                ExposedDropdownMenu(modifier = Modifier.fillMaxWidth(),
                    expanded = dayIsExpanded,
                    onDismissRequest = {
                        dayIsExpanded = false
                    }) {
                    dayList.forEach { day ->
                        DropdownMenuItem(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                selectedDay = day
                                dayIsExpanded = false
                            },
                            text = {
                                Text(text = day)
                            },
                        )
                    }
                }
            }
            OutlinedTextField(
                modifier = modifier,
                readOnly = true,
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
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
                    if (!loading) {
                        if (selectedDepartment.departmentId != 0 && selectedProgram.isNotBlank() && selectedLevel.levelId > 0 && courseName.isNotBlank() && selectedDay.isNotBlank() && time
                                .isNotBlank()
                        ) {
                            coroutineScope.launch {
                                try {
                                    loading = true
                                    viewModel.addClassSchedule(
                                        AddClassScheduleRequest(
                                            selectedDepartment.departmentId,
                                            selectedProgram.trim(),
                                            selectedLevel.levelId,
                                            courseName.trim(),
                                            selectedDay,
                                            time,
                                            viewModel.user?.userId ?: 0
                                        )
                                    )
                                    Toast.makeText(
                                        context,
                                        "Class schedule added successfully!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navController.popBackStack()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                loading = false

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
                        Text("Adding facility...", modifier = Modifier.padding(4.dp))
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

            if (displayTimeDialog) {
                Dialog(onDismissRequest = { displayTimeDialog = false }) {
                    Card(
                        modifier = Modifier
                            .padding(defaultPadding),
                    ) {
                        Column(
                            modifier = Modifier,
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            TimePicker(
                                state = timeState,
                                modifier = Modifier.padding(defaultPadding)
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
    }
}

@Preview(showBackground = true)
@Composable
fun AddClassSchedulePreview() {
    SmartNavigationTheme {
        AddClassScheduleScreen(rememberNavController(), MainViewModel())
    }
}