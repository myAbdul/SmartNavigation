package com.example.smartnavigation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.smartnavigation.MainViewModel
import com.example.smartnavigation.navigate.NavRoutes
import com.example.smartnavigation.theme.SmartNavigationTheme
import com.example.smartnavigation.theme.defaultPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: MainViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Home")
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.facilityImage = null
                            navController.navigate(NavRoutes.ADD_FACILITY)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(defaultPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    SmartNavigationTheme {
        HomeScreen(rememberNavController(), MainViewModel())
    }
}