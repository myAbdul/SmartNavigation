package com.example.smartnavigation.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.smartnavigation.MainViewModel
import com.example.smartnavigation.decodeImage
import com.example.smartnavigation.navigate.NavRoutes
import com.example.smartnavigation.theme.SmartNavigationTheme
import com.example.smartnavigation.theme.defaultPadding

@Composable
fun FacilitiesScreen(navController: NavHostController, viewModel: MainViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(defaultPadding)
    ) {
        var loading by remember { mutableStateOf(false) }
        val context = LocalContext.current

        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 48.dp)
                    .width(24.dp)
                    .height(24.dp)
                    .align(Alignment.TopCenter)
            )
        } else {
            LazyColumn(
                Modifier
                    .padding(defaultPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(viewModel.facilityList) { facility ->
                    val image = facility.image?.let { decodeImage(it) }
                    Card(
                        modifier = Modifier
                            .padding(defaultPadding)
                            .fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(defaultPadding)
                        ) {
                            Row(
                                Modifier
                                    .padding(defaultPadding)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(modifier = Modifier.padding(end = 8.dp), text = facility.name)
                                Icon(
                                    modifier = Modifier.clickable {
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            data =
                                                Uri.parse("geo:${facility.latitude},${facility.longitude}")
                                        }
                                        context.startActivity(intent)
                                    },
                                    imageVector = Icons.Filled.LocationOn,
                                    contentDescription = ""
                                )
                            }
                            if (image != null) {
                                Image(
                                    modifier = Modifier
                                        .padding(defaultPadding)
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    bitmap = image.asImageBitmap(),
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Image(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    imageVector = Icons.Filled.Image,
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop
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
                    viewModel.facilityName = ""
                    viewModel.facilityImage = null
                    navController.navigate(NavRoutes.ADD_FACILITY)
                },
            ) {
                Icon(Icons.Filled.Add, "Add facility")
            }
        }

        LaunchedEffect(true) {
            loading = true
            viewModel.getAllFacilities()
            loading = false
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FacilitiesPreview() {
    SmartNavigationTheme {
        FacilitiesScreen(rememberNavController(), MainViewModel())
    }
}