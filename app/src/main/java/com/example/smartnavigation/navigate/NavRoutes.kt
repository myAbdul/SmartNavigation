package com.example.smartnavigation.navigate

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartnavigation.MainViewModel
import com.example.smartnavigation.view.AddCampusEventScreen
import com.example.smartnavigation.view.AddClassScheduleScreen
import com.example.smartnavigation.view.AddFacilityScreen
import com.example.smartnavigation.view.CameraScreen
import com.example.smartnavigation.view.HomeScreen
import com.example.smartnavigation.view.LoginScreen
import com.example.smartnavigation.view.RegisterScreen

object NavRoutes {
    const val REGISTER = "register"
    const val LOGIN = "login"
    const val HOME = "home"
    const val ADD_FACILITY = "add_facility"
    const val ADD_CLASS_SCHEDULE = "add_class_schedule"
    const val ADD_CAMPUS_EVENT = "add_campus_event"
    const val CAMERA = "camera"
}

@Composable
fun NavHost(
    navController: NavHostController, viewModel: MainViewModel
) {
    NavHost(navController = navController, startDestination = NavRoutes.LOGIN) {
        composable(NavRoutes.LOGIN) {
            LoginScreen(navController, viewModel)
        }
        composable(NavRoutes.REGISTER) {
            RegisterScreen(navController, viewModel)
        }
        composable(NavRoutes.HOME) {
            HomeScreen(navController, viewModel)
        }
        composable(NavRoutes.ADD_FACILITY) {
            AddFacilityScreen(navController, viewModel)
        }
        composable(NavRoutes.CAMERA) {
            CameraScreen(navController, viewModel)
        }
        composable(NavRoutes.ADD_CLASS_SCHEDULE) {
            AddClassScheduleScreen(navController, viewModel)
        }
        composable(NavRoutes.ADD_CAMPUS_EVENT) {
            AddCampusEventScreen(navController, viewModel)
        }
    }
}