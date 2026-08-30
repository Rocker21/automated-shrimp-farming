package com.example.shrimpcaring.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shrimpcaring.navigation.Screen
import com.example.shrimpcaring.viewmodel.PondViewModel
import com.example.shrimpcaring.models.Pond

@Composable
fun HomeScreen(
    navController: NavController,
    pondViewModel: PondViewModel = viewModel()
) {
    val ponds by pondViewModel.serverPonds.collectAsState()
    LaunchedEffect(Unit) {
        pondViewModel.loadServerPonds()
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    var pondToDelete by remember { mutableStateOf<Pond?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                }
            ) {
                Icon(
                    Icons.Default.Add,
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
                "Shrimp Caring",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            val serverError by pondViewModel.serverError.collectAsState()
            if (serverError != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Text(
                            text = serverError!!,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { pondViewModel.clearServerError() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear Error"
                            )
                        }
                    }
                }
            }

            if (ponds.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("No ponds added yet. Tap + to add one.")
                }
            } else {
                LazyColumn {
                    items(ponds) { pond ->
                        PondCard(
                            pond = pond,
                            onClick = {
                                navController.navigate(
                                    Screen.DeviceSelection.createRoute(pond.id)
                                )
                            },
                            onDelete = {
                                pondToDelete = pond
                            }
                        )
                    }
                }
            }
        }
    }

    if (pondToDelete != null) {
        AlertDialog(
            onDismissRequest = { pondToDelete = null },
            title = { Text("Delete Pond") },
            text = { Text("Are you sure you want to delete ${pondToDelete?.name}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        pondToDelete?.let { pondViewModel.deletePond(it) }
                        pondToDelete = null
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { pondToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showDialog) {
        AddPondDialog(
            onDismiss = {
                showDialog = false
            },
            onSave = { name, location ->
                pondViewModel.addPond(name, location)
                showDialog = false
            }
        )
    }
}
