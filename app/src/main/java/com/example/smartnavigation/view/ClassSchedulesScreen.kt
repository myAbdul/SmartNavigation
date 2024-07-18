package com.example.smartnavigation.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.smartnavigation.MainViewModel
import com.example.smartnavigation.model.College
import com.example.smartnavigation.model.Department
import com.example.smartnavigation.model.Level
import com.example.smartnavigation.model.Program
import com.example.smartnavigation.navigate.NavRoutes
import com.example.smartnavigation.theme.SmartNavigationTheme
import com.example.smartnavigation.theme.defaultPadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassSchedulesScreen(navController: NavHostController, viewModel: MainViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val emptyDepartment = Department(0, 0, "")
    val emptyCollege = College(0, "")
    val emptyProgram = Program(0, 0, "")
    val emptyLevel = Level(0, "")
    var collegeIsExpanded by remember { mutableStateOf(false) }
    var selectedCollege by remember { mutableStateOf(emptyCollege) }
    var departmentIsExpanded by remember { mutableStateOf(false) }
    var selectedDepartment by remember { mutableStateOf(emptyDepartment) }
    var programIsExpanded by remember { mutableStateOf(false) }
    var selectedProgram by remember { mutableStateOf(emptyProgram) }
    var levelIsExpanded by remember { mutableStateOf(false) }
    var selectedLevel by remember { mutableStateOf(emptyLevel) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(defaultPadding)
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
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
                                    viewModel.programList.firstOrNull { it.departmentId == selectedDepartment.departmentId }
                                        ?: emptyProgram

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
                                        viewModel.programList.firstOrNull { it.departmentId == selectedDepartment.departmentId }
                                            ?: emptyProgram
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
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = selectedProgram.name,
                    onValueChange = { },
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
                                    selectedProgram = option
                                    programIsExpanded = false
                                },
                                text = {
                                    Text(text = option.name)
                                },
                            )
                        }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(defaultPadding),
                verticalAlignment = Alignment.Bottom
            ) {
                ExposedDropdownMenuBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
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
                Button(
                    modifier = Modifier.padding(start = 16.dp),
                    onClick = {
                        coroutineScope.launch {
                            Log.d("Abdul", "ClassSchedulesScreen: ${selectedLevel}")
                            viewModel.getClassSchedules(
                                selectedProgram.programId, selectedLevel.levelId
                            )
                        }
                    },
                ) {
                    Text("Load", modifier = Modifier.padding(4.dp))
                }
            }

            if (viewModel.loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 48.dp)
                        .width(24.dp)
                        .height(24.dp)
                )
            } else {
                if (viewModel.classScheduleList.isEmpty()) {
                    Text(
                        modifier = Modifier.padding(top = 48.dp),
                        text = "No class schedule found...",
                    )
                } else {
                    LazyColumn(
                        Modifier
                            .padding(defaultPadding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        items(viewModel.classScheduleList) { classSchedule ->
                            Card(
                                modifier = Modifier
                                    .padding(defaultPadding)
                                    .fillMaxWidth(),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "Course Name: ${classSchedule.courseName}"
                                    )
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 4.dp),
                                        text = "Day: ${classSchedule.day}"
                                    )
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 4.dp),
                                        text = "Time: ${classSchedule.time}"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .padding(defaultPadding)
                .align(Alignment.BottomEnd),
            onClick = {
                navController.navigate(NavRoutes.ADD_CLASS_SCHEDULE)
            },
        ) {
            Icon(Icons.Filled.Add, "Add class schedule")
        }
    }

    LaunchedEffect(true) {
        viewModel.getClassScheduleFormData()
        selectedCollege = viewModel.collegeList.firstOrNull() ?: emptyCollege
        selectedDepartment =
            viewModel.departmentList.firstOrNull { it.collegeId == selectedCollege.collegeId }
                ?: emptyDepartment
        selectedProgram =
            viewModel.programList.firstOrNull { it.departmentId == selectedDepartment.departmentId }
                ?: emptyProgram
        selectedLevel = viewModel.levelList.firstOrNull() ?: emptyLevel
    }
}

@Preview(showBackground = true)
@Composable
fun ClassSchedulesPreview() {
    SmartNavigationTheme {
        ClassSchedulesScreen(rememberNavController(), MainViewModel())
    }
}