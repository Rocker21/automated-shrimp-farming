package com.example.shrimpcaring.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PowerCard(
    voltage: String,
    current: String,
    power: String,
    energy: String,
    frequency: String,
    pf: String
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                "Power Statistics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Voltage : $voltage V")
            Text("Current : $current A")
            Text("Power : $power W")
            Text("Energy : $energy kWh")
            Text("Frequency : $frequency Hz")
            Text("Power Factor : $pf")
        }
    }
}