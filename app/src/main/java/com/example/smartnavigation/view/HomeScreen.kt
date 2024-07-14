package com.example.smartnavigation.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon as IconM1
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text as TextM1
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.smartnavigation.MainViewModel
import com.example.smartnavigation.theme.Purple40
import com.example.smartnavigation.theme.SmartNavigationTheme

sealed class HomeScreen(val route: String, val title: String, val imageVector: ImageVector) {
    data object ClassSchedules : HomeScreen("classSchedules", "Timetable", Icons.Filled.Class)
    data object Facilities : HomeScreen("facilities", "Facilities", Icons.Filled.Business)
    data object CampusEvents : HomeScreen("campusEvents", "Events", Icons.Filled.Event)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: MainViewModel) {
    val screenList = listOf(
        HomeScreen.ClassSchedules,
        HomeScreen.Facilities,
        HomeScreen.CampusEvents
    )
    val homeNavController = rememberNavController()
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var currentScreen by remember { mutableStateOf(screenList.first()) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(currentScreen.title)
                }
            )
        },
        bottomBar = {
            BottomNavigation(
                contentColor = Color.White,
                backgroundColor = Purple40
            ) {
                screenList.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            IconM1(
                                screen.imageVector, contentDescription = null
                            )
                        },
                        label = { TextM1(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            homeNavController.navigate(screen.route) {
                                popUpTo(homeNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            currentScreen = screen
                        },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.White.copy(alpha = 0.5F)
                    )
                }
            }
        }) { innerPadding ->
        NavHost(
            homeNavController,
            startDestination = HomeScreen.ClassSchedules.route,
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(HomeScreen.ClassSchedules.route) {
                ClassSchedulesScreen(navController, viewModel)
            }
            composable(HomeScreen.Facilities.route) {
                FacilitiesScreen(navController, viewModel)
            }
            composable(HomeScreen.CampusEvents.route) {
                CampusEventsScreen(navController, viewModel)
            }
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