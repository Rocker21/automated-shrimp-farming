package com.example.shrimpcaring.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SetupScreen(
    onBluetooth: () -> Unit,
    onWifi: (String, String) -> Unit
) {
    var ssid by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Setup",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onBluetooth
        ) {
            Text("Connect Bluetooth")
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = ssid,
            onValueChange = { ssid = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("WiFi SSID") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("WiFi Password") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onWifi(ssid, password) }
        ) {
            Text("Send WiFi")
        }
    }
}
