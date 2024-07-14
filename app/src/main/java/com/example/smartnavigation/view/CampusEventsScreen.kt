package com.example.smartnavigation.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.smartnavigation.MainViewModel
import com.example.smartnavigation.navigate.NavRoutes
import com.example.smartnavigation.theme.SmartNavigationTheme
import com.example.smartnavigation.theme.defaultPadding

@Composable
fun CampusEventsScreen(navController: NavHostController, viewModel: MainViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(defaultPadding)
    ) {
        val context = LocalContext.current

        if (viewModel.loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 48.dp)
                    .width(24.dp)
                    .height(24.dp)
                    .align(Alignment.TopCenter)
            )
        } else {
            if (viewModel.campusEventList.isEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(defaultPadding)
                        .align(Alignment.TopCenter),
                    text = "No campus event found...",
                )
            } else {
                LazyColumn(
                    Modifier
                        .padding(defaultPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    items(viewModel.campusEventList) { campusEvent ->
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
                                Row(
                                    Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        modifier = Modifier.padding(end = 8.dp),
                                        text = campusEvent.name
                                    )
                                    Icon(
                                        modifier = Modifier.clickable {
                                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                                data =
                                                    Uri.parse("geo:${campusEvent.facilityLatitude},${campusEvent.facilityLongitude}")
                                            }
                                            context.startActivity(intent)
                                        },
                                        imageVector = Icons.Outlined.LocationOn,
                                        contentDescription = ""
                                    )
                                }
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    text = "Date: ${campusEvent.date}"
                                )
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    text = "Time: ${campusEvent.time}"
                                )
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    text = "Venue: ${campusEvent.facilityName}"
                                )
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
                    navController.navigate(NavRoutes.ADD_CAMPUS_EVENT)
                },
            ) {
                Icon(Icons.Filled.Add, "Add facility")
            }
        }

        LaunchedEffect(true) {
            viewModel.getCampusEvents()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CampusEventsPreview() {
    SmartNavigationTheme {
        CampusEventsScreen(rememberNavController(), MainViewModel())
    }
}