package com.example.shrimpcaring.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.shrimpcaring.navigation.Screen

@Composable
fun HomeScreen(
    navController: NavHostController
) {

    val ponds = listOf(
        "Pond 1",
        "Pond 2",
        "Pond 3"
    )

    Scaffold(

        floatingActionButton = {

            FloatingActionButton(
                onClick = {
                    // TODO: Add Pond Screen
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Pond"
                )
            }

        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Shrimp Ponds",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {

                items(ponds) { pond ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {

                                navController.navigate(
                                    Screen.Dashboard.route
                                )

                            }
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {

                            Icon(
                                imageVector = Icons.Default.Water,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = pond,
                                style = MaterialTheme.typography.titleLarge
                            )

                        }

                    }

                }

            }

        }

    }

}