package com.example.shrimpcaring.screens.devices

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeviceHub
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shrimpcaring.navigation.Screen
import com.example.shrimpcaring.viewmodel.PondViewModel


data class DeviceItem(
    val title: String,
    val route: String,
    val isAerator: Boolean = false
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceSelectionScreen(
    navController: NavController,
    pondId: Int,
    pondViewModel: PondViewModel = viewModel()
) {

    // =====================================================
    // SERVER AERATOR COUNT
    // =====================================================

    val aeratorCount by
    pondViewModel.serverAeratorCount.collectAsState()

    val serverError by
    pondViewModel.serverError.collectAsState()


    // =====================================================
    // LOAD AERATOR CONFIGURATION
    // =====================================================

    LaunchedEffect(pondId) {

        pondViewModel.loadServerAeratorCount(
            pondId
        )
    }


    // =====================================================
    // DEVICE LIST
    // =====================================================

    val devices = listOf(

        DeviceItem(
            title = "🌀 Aerators",
            route = "",
            isAerator = true
        ),

        DeviceItem(
            title = "🧪 pH Sensor",
            route = Screen.Ph.createRoute(pondId)
        ),

        DeviceItem(
            title = "💧 DO Sensor",
            route = "do/$pondId"
        ),

        DeviceItem(
            title = "🌡 Temperature Sensor",
            route = "temperature/$pondId"
        )
    )


    // =====================================================
    // SCREEN
    // =====================================================

    Scaffold { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            // =================================================
            // SERVER ERROR
            // =================================================

            if (serverError != null) {

                item {

                    Text(
                        text = serverError
                            ?: "Unable to load devices",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(
                        Modifier.height(8.dp)
                    )
                }
            }


            // =================================================
            // DEVICES
            // =================================================

            items(devices) { device ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {

                            if (device.isAerator) {

                                /*
                                 * null = still checking server
                                 *
                                 * Don't navigate until we know
                                 * whether aerators exist.
                                 */

                                when {

                                    aeratorCount == null -> {

                                        // Still loading.
                                        // Do nothing.
                                    }


                                    aeratorCount!! > 0 -> {

                                        /*
                                         * Aerators already exist.
                                         *
                                         * Go DIRECTLY to control
                                         * screen.
                                         */

                                        navController.navigate(
                                            Screen.Aerator.createRoute(
                                                pondId
                                            )
                                        )
                                    }


                                    else -> {

                                        /*
                                         * No aerators exist.
                                         *
                                         * Ask how many aerators
                                         * only the FIRST time.
                                         */

                                        navController.navigate(
                                            Screen.AeratorCount.createRoute(
                                                pondId
                                            )
                                        )
                                    }
                                }

                            } else {

                                navController.navigate(
                                    device.route
                                )
                            }
                        }
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        // =========================================
                        // DEVICE ICON
                        // =========================================

                        Icon(
                            imageVector =
                                Icons.Default.DeviceHub,
                            contentDescription = null
                        )


                        Spacer(
                            Modifier.width(16.dp)
                        )


                        // =========================================
                        // DEVICE NAME
                        // =========================================

                        Text(
                            text = device.title,
                            style =
                                MaterialTheme.typography.titleLarge,
                            modifier =
                                Modifier.weight(1f)
                        )


                        // =========================================
                        // AERATOR LOADING
                        // =========================================

                        if (
                            device.isAerator &&
                            aeratorCount == null
                        ) {

                            CircularProgressIndicator(
                                modifier =
                                    Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }


                        // =========================================
                        // EDIT AERATORS
                        // =========================================

                        if (
                            device.isAerator &&
                            aeratorCount != null &&
                            aeratorCount!! > 0
                        ) {

                            IconButton(
                                onClick = {

                                    /*
                                     * IMPORTANT:
                                     *
                                     * This click deliberately opens
                                     * AeratorCountScreen so the user
                                     * can change the configuration.
                                     */

                                    navController.navigate(
                                        Screen.AeratorCount.createRoute(
                                            pondId
                                        )
                                    )
                                }
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Default.Edit,
                                    contentDescription =
                                        "Edit Aerators"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}