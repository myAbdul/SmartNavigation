package com.example.smartnavigation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.smartnavigation.MainViewModel
import com.example.smartnavigation.api.request.LoginRequest
import com.example.smartnavigation.navigate.NavRoutes
import com.example.smartnavigation.theme.SmartNavigationTheme
import com.example.smartnavigation.theme.defaultPadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController, viewModel: MainViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Login")
                },
            )
        },
    ) { innerPadding ->
        val modifier = Modifier
            .padding(defaultPadding)
            .fillMaxWidth()
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val coroutineScope = rememberCoroutineScope()
        var showErrorDialog by remember { mutableStateOf(false) }
        Column(
            Modifier
                .padding(innerPadding)
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = modifier,
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
            )
            OutlinedTextField(
                modifier = modifier,
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Button(
                modifier = modifier,
                onClick = {
                    if (!viewModel.loading) {
                        coroutineScope.launch {
                            val result = viewModel.login(LoginRequest(username, password))
                            if (result){
                                navController.navigate(NavRoutes.HOME)
                            } else{
                                showErrorDialog=true
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
                        Text("Signing in...", modifier = Modifier.padding(4.dp))
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .width(24.dp)
                                .height(24.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    } else {
                        Text("Login", modifier = Modifier.padding(4.dp))
                    }
                }
            }
            TextButton(onClick = {
                navController.navigate(NavRoutes.REGISTER)
            }) {
                Text("Register")
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
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    SmartNavigationTheme {
        LoginScreen(rememberNavController(), MainViewModel())
    }
}