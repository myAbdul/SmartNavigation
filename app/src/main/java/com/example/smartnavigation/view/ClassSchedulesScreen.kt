package com.example.smartnavigation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.smartnavigation.MainViewModel
import com.example.smartnavigation.theme.SmartNavigationTheme
import com.example.smartnavigation.theme.defaultPadding

@Composable
fun ClassSchedulesScreen(navController: NavHostController, viewModel: MainViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(defaultPadding)
    ) {}
}

@Preview(showBackground = true)
@Composable
fun ClassSchedulesPreview() {
    SmartNavigationTheme {
        ClassSchedulesScreen(rememberNavController(), MainViewModel())
    }
}