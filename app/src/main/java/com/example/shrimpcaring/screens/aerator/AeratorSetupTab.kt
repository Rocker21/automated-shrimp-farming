package com.example.shrimpcaring.screens.aerator

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shrimpcaring.viewmodel.AeratorViewModel
import com.example.shrimpcaring.viewmodel.MainViewModel
import com.example.shrimpcaring.viewmodel.WifiViewModel

@SuppressLint("MissingPermission")
@Composable
fun AeratorSetupTab(
    pondId: Int,
    viewModel: AeratorViewModel = viewModel(),
    wifiViewModel: WifiViewModel = viewModel(),
    mainViewModel: MainViewModel = viewModel()
) {
    val isScanning by viewModel.isScanning.collectAsState()
    val foundDevices by viewModel.foundDevices.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()
    val connectedDevice by viewModel.connectedDevice.collectAsState()
    
    val savedWifiList by wifiViewModel.savedWifiList.collectAsState(initial = emptyList())

    var ssid by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var deviceName by remember { mutableStateOf("ESP32 Aerator") }

    var interval by remember {
        mutableStateOf("5 sec")
    }
    
    var showSavedWifiDialog by remember { mutableStateOf(false) }

    // Load existing config
    val context = androidx.compose.ui.platform.LocalContext.current
    LaunchedEffect(pondId) {
        val db = com.example.shrimpcaring.di.ServiceLocator.provideDatabase(context)
        db.configurationDao().getConfiguration(pondId).collect { config ->
            config?.let {
                ssid = it.wifiSSID
                password = it.wifiPassword
                interval = when (it.samplingInterval) {
                    5 -> "5 sec"
                    10 -> "10 sec"
                    30 -> "30 sec"
                    60 -> "1 min"
                    300 -> "5 min"
                    else -> "10 sec"
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            "Bluetooth",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(10.dp))

        if (statusMessage.isNotEmpty()) {
            Text(
                text = statusMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = { viewModel.startScan() },
            enabled = !isScanning
        ) {
            if (isScanning) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
                Spacer(Modifier.width(8.dp))
                Text("Scanning...")
            } else {
                Text("Scan for ESP32")
            }
        }

        if (foundDevices.isNotEmpty() && connectedDevice == null) {
            Spacer(Modifier.height(10.dp))
            Text("Available Devices:", style = MaterialTheme.typography.titleSmall)
            foundDevices.forEach { device ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { viewModel.connectToDevice(device) }
                ) {
                    Text(
                        text = device.name ?: "Unknown Device",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

        if (connectedDevice != null) {
            Spacer(Modifier.height(10.dp))
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Connected to: ${connectedDevice?.name ?: "Unknown"}",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    TextButton(onClick = { viewModel.disconnect() }) {
                        Text("Disconnect")
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "WiFi",
                style = MaterialTheme.typography.titleLarge
            )
            
            TextButton(onClick = { showSavedWifiDialog = true }) {
                Icon(Icons.Default.Wifi, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Saved WiFi")
            }
        }

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = ssid,
            onValueChange = { ssid = it },
            label = { Text("SSID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(Modifier.height(8.dp))
        
        Button(
            onClick = { wifiViewModel.saveWifi(ssid, password) },
            modifier = Modifier.align(Alignment.End),
            enabled = ssid.isNotEmpty()
        ) {
            Icon(Icons.Default.Save, contentDescription = null)
            Spacer(Modifier.width(4.dp))
            Text("Save this network")
        }

        Spacer(Modifier.height(24.dp))

        Text(
            "Device Settings",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = deviceName,
            onValueChange = { deviceName = it },
            label = { Text("Device Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Text(
            "Sampling Interval",
            style = MaterialTheme.typography.titleLarge
        )

        listOf(
            "5 sec",
            "10 sec",
            "30 sec",
            "1 min",
            "5 min"
        ).forEach { item ->
            Row {
                RadioButton(
                    selected = interval == item,
                    onClick = { interval = item }
                )
                Text(
                    text = item,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = connectedDevice != null,
            onClick = {
                viewModel.saveConfiguration(
                    pondId = pondId,
                    ssid = ssid,
                    password = password,
                    deviceName = deviceName,
                    intervalString = interval
                )
                mainViewModel.setActivePond(pondId)
            }
        ) {
            Text(if (connectedDevice != null) "Send Config to ESP32" else "Connect Device to Provision")
        }
    }
    
    if (showSavedWifiDialog) {
        AlertDialog(
            onDismissRequest = { showSavedWifiDialog = false },
            title = { Text("Select Saved WiFi") },
            text = {
                if (savedWifiList.isEmpty()) {
                    Text("No saved networks.")
                } else {
                    Column {
                        savedWifiList.forEach { wifi ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        ssid = wifi.ssid
                                        password = wifi.password
                                        showSavedWifiDialog = false
                                    }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Wifi, contentDescription = null)
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(wifi.ssid, style = MaterialTheme.typography.bodyLarge)
                                    Text("Password: ****", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            HorizontalDivider()
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSavedWifiDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}
