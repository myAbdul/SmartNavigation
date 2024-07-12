package com.example.smartnavigation.view

import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Class
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.example.smartnavigation.theme.SmartNavigationTheme

sealed class HomeScreen(val route: String, val title: String, val imageVector: ImageVector) {
    object ClassSchedules : HomeScreen("classSchedules", "Timetable", Icons.Filled.Class)
    object Facilities : HomeScreen("facilities", "Facilities", Icons.Filled.Business)
}

val items = listOf(
    HomeScreen.ClassSchedules,
    HomeScreen.Facilities,
)

@Composable
fun HomeScreen(navController: NavHostController, viewModel: MainViewModel) {
    val homeNavController = rememberNavController()
    Scaffold(bottomBar = {
        BottomNavigation {
            val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            items.forEach { screen ->
                BottomNavigationItem(icon = {
                    Icon(
                        screen.imageVector, contentDescription = null
                    )
                },
                    label = { Text(screen.title) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        homeNavController.navigate(screen.route) {
                            popUpTo(homeNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
            }
        }
    }) { innerPadding ->
        NavHost(
            homeNavController,
            startDestination = HomeScreen.ClassSchedules.route,
            Modifier.padding(innerPadding)
        ) {
            composable(HomeScreen.ClassSchedules.route) {
                ClassSchedulesScreen(
                    navController, viewModel
                )
            }
            composable(HomeScreen.Facilities.route) { FacilitiesScreen(navController, viewModel) }
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