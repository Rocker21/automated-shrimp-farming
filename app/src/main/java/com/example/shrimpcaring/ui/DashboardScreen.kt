package com.example.shrimpcaring.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(
    ph: String,
    voltage: String,
    current: String,
    power: String,
    energy: String,
    frequency: String,
    pf: String,
    online: Boolean
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            text = "Shrimp Caring Dashboard",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        StatusCard(
            title = "System Status",
            value = if (online) "ONLINE" else "OFFLINE"
        )

        Spacer(modifier = Modifier.height(16.dp))

        SensorCard("pH", ph)

        SensorCard("Voltage", "$voltage V")

        SensorCard("Current", "$current A")

        SensorCard("Power", "$power W")

        SensorCard("Energy", "$energy kWh")

        SensorCard("Frequency", "$frequency Hz")

        SensorCard("Power Factor", pf)

    }

}

@Composable
private fun SensorCard(
    title: String,
    value: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium
            )

        }

    }

}

@Composable
private fun StatusCard(
    title: String,
    value: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall
            )

        }

    }

}