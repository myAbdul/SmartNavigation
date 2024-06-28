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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.smartnavigation.api.register.RegisterRequest
import com.example.smartnavigation.theme.SmartNavigationTheme
import com.example.smartnavigation.theme.defaultPadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController, viewModel: MainViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Register")
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
        var firstName by remember { mutableStateOf("") }
        var lastName by remember { mutableStateOf("") }
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var loading by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        Column(
            modifier
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = modifier,
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
            )
            OutlinedTextField(
                modifier = modifier,
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
            )
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
            OutlinedTextField(
                modifier = modifier,
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Button(
                modifier = modifier,
                onClick = {
                    if (!loading) {
                        coroutineScope.launch {
                            if (validateInputs(
                                    firstName,
                                    lastName,
                                    username,
                                    password,
                                    confirmPassword
                                )
                            ) {
                                loading = true
                                try {
                                    viewModel.register(
                                        RegisterRequest(
                                            firstName,
                                            lastName,
                                            username,
                                            password
                                        )
                                    )
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
                        Text("Registering...")
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(8.dp)
                                .width(24.dp)
                                .height(24.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    } else {
                        Text("Register")
                    }
                }
            }
        }
    }
}

private fun validateInputs(
    firstName: String,
    lastName: String,
    username: String,
    password: String,
    confirmPassword: String,
): Boolean {
    return if (firstName.isBlank()) {
        false
    } else if (lastName.isBlank()) {
        false
    } else if (username.trim().length < 3) {
        false
    } else if (username.trim().any { !it.isLetterOrDigit() }) {
        false
    } else if (password.trim().length < 5 || password != confirmPassword) {
        false
    } else {
        true
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    SmartNavigationTheme {
        RegisterScreen(rememberNavController(), MainViewModel())
    }
}