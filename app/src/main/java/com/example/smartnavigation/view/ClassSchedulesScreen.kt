package com.example.smartnavigation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.smartnavigation.MainViewModel
import com.example.smartnavigation.model.College
import com.example.smartnavigation.model.Department
import com.example.smartnavigation.model.Program
import com.example.smartnavigation.theme.SmartNavigationTheme
import com.example.smartnavigation.theme.defaultPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassSchedulesScreen(navController: NavHostController, viewModel: MainViewModel) {
    var collegeIsExpanded by remember { mutableStateOf(false) }
    var selectedCollege by remember { mutableStateOf(College(0, "")) }
    var departmentIsExpanded by remember { mutableStateOf(false) }
    var selectedDepartment by remember { mutableStateOf(Department(0, 0, "")) }
    var programIsExpanded by remember { mutableStateOf(false) }
    var selectedProgram by remember { mutableStateOf(Program(0, 0, "")) }
    Column(
        Modifier
            .fillMaxSize()
            .padding(defaultPadding)
    ) {
        ExposedDropdownMenuBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(defaultPadding),
            expanded = collegeIsExpanded,
            onExpandedChange = { collegeIsExpanded = it },
        ) {
            TextField(
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                value = selectedCollege.name,
                onValueChange = { },
                label = { Text("College") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = collegeIsExpanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
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
                            collegeIsExpanded = false
                        },
                        text = {
                            Text(text = option.name)
                        },
                    )
                }
            }
        }
    }

    LaunchedEffect(true) {
        viewModel.getClassScheduleFormData()
        viewModel.collegeList.firstOrNull()?.let {
            selectedCollege = it
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClassSchedulesPreview() {
    SmartNavigationTheme {
        ClassSchedulesScreen(rememberNavController(), MainViewModel())
    }
}