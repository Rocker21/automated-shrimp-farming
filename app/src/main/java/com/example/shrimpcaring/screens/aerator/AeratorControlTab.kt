package com.example.shrimpcaring.screens.aerator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shrimpcaring.viewmodel.MainViewModel
import com.example.shrimpcaring.viewmodel.PondViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AeratorControlTab(
    pondId: Int,
    mainViewModel: MainViewModel = viewModel(),
    pondViewModel: PondViewModel = viewModel(),
    onGoToSetup: () -> Unit = {}
) {

    // =====================================================
    // SERVER AERATOR COUNT
    // =====================================================

    val aeratorCount by pondViewModel.serverAeratorCount.collectAsState()


    // =====================================================
    // LOAD POND AERATORS
    // =====================================================

    LaunchedEffect(pondId) {
        pondViewModel.loadServerAeratorCount(pondId)
        mainViewModel.setActivePond(pondId)
    }


    // =====================================================
    // PZEM DATA
    // =====================================================

    val voltage by mainViewModel.voltage.collectAsState()
    val current by mainViewModel.current.collectAsState()
    val power by mainViewModel.power.collectAsState()
    val energy by mainViewModel.energy.collectAsState()
    val frequency by mainViewModel.frequency.collectAsState()
    val powerFactor by mainViewModel.powerFactor.collectAsState()
    val pzemConnected by mainViewModel.pzemConnected.collectAsState()
    val rawBleData by mainViewModel.rawBleData.collectAsState()


    // =====================================================
    // LAST UPDATE
    // =====================================================

    val lastUpdate = remember { mutableLongStateOf(0L) }

    LaunchedEffect(voltage, frequency, powerFactor, energy, current, power) {
        lastUpdate.longValue = System.currentTimeMillis()
    }

    val timeFormat = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }


    // =====================================================
    // RELAY STATES
    // =====================================================

    val relay1 by mainViewModel.relay1.collectAsState()
    val relay2 by mainViewModel.relay2.collectAsState()
    val relay3 by mainViewModel.relay3.collectAsState()
    val relay4 by mainViewModel.relay4.collectAsState()

    val relayStates = listOf(relay1, relay2, relay3, relay4)

    val relaySetters = listOf<(Boolean) -> Unit>(
        { mainViewModel.setRelay1(it) },
        { mainViewModel.setRelay2(it) },
        { mainViewModel.setRelay3(it) },
        { mainViewModel.setRelay4(it) }
    )


    // =====================================================
    // SCREEN
    // =====================================================

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // =================================================
        // ELECTRICAL PARAMETERS
        // =================================================

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Electrical Parameters",
                            style = MaterialTheme.typography.titleLarge
                        )
                        
                        val bleConnected by mainViewModel.bleConnected.collectAsState()
                        if (bleConnected) {
                            Surface(
                                color = Color(0xFF4CAF50),
                                shape = MaterialTheme.shapes.extraSmall
                            ) {
                                Text(
                                    text = "BLE LIVE",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        
                        IconButton(onClick = { mainViewModel.requestSensorData() }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh Data",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    ParameterRow(name = "Voltage", value = "$voltage V")
                    ParameterRow(name = "Frequency", value = "$frequency Hz")
                    ParameterRow(name = "Power Factor", value = "$powerFactor")
                    ParameterRow(name = "Energy", value = "$energy kWh")

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (lastUpdate.longValue > 0) {
                            "Last update: ${timeFormat.format(Date(lastUpdate.longValue))}"
                        } else {
                            "Waiting for electrical data..."
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    
                    if (rawBleData.isNotEmpty()) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Raw: $rawBleData",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // =================================================
        // LOADING AERATORS
        // =================================================

        if (aeratorCount == null) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Loading aerators...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        // =================================================
        // NO AERATORS
        // =================================================

        else if (aeratorCount == 0) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "No aerators configured for this pond.",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = onGoToSetup) {
                                Text("Setup ESP32 Device")
                            }
                        }
                    }
                }
            }
        }

        // =================================================
        // AERATORS
        // =================================================

        else {
            items(count = minOf(aeratorCount ?: 0, 4)) { index ->
                val relayNumber = index + 1
                val isOn = relayStates[index]

                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = isOn,
                            onCheckedChange = { relaySetters[index](it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF4CAF50),
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFFF44336),
                                uncheckedBorderColor = Color(0xFFF44336)
                            )
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Aerator $relayNumber",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "Relay : $relayNumber",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (relayNumber == 1) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Electrical monitoring",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = if (isOn) "ON" else "OFF",
                                color = if (isOn) Color(0xFF4CAF50) else Color(0xFFF44336),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            if (relayNumber == 1) {
                                Text(text = "$current A", style = MaterialTheme.typography.bodyMedium)
                                Text(text = "$power W", style = MaterialTheme.typography.bodyMedium)
                            } else {
                                Text(
                                    text = "-- A",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "-- W",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ParameterRow(name: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}
