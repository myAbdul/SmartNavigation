package com.example.smartnavigation.view

import android.widget.Toast
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.smartnavigation.MainViewModel
import com.example.smartnavigation.theme.SmartNavigationTheme
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
        val context = LocalContext.current
        var firstName by remember { mutableStateOf("") }
        var lastName by remember { mutableStateOf("") }
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var loading by remember { mutableStateOf(false) }
        var showErrorDialog by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        Column(
            modifier.padding(innerPadding),
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
                            val result = viewModel.register(
                                firstName, lastName, username, password, confirmPassword
                            )
                            if (result) {
                                navController.popBackStack()
                                Toast.makeText(
                                    context, "User registered successfully!", Toast.LENGTH_LONG
                                ).show()
                            } else {
                                showErrorDialog = true
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
                        Text("Registering...", modifier = Modifier.padding(4.dp))
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .width(24.dp)
                                .height(24.dp),
                        )
                    } else {
                        Text("Register", modifier = Modifier.padding(4.dp))
                    }
                }
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
fun RegisterPreview() {
    SmartNavigationTheme {
        RegisterScreen(rememberNavController(), MainViewModel())
    }
}