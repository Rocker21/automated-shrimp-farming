package com.example.shrimpcaring.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ControlScreen(
    relay1: Boolean,
    relay2: Boolean,
    relay3: Boolean,
    relay4: Boolean,
    onRelay1: (Boolean) -> Unit,
    onRelay2: (Boolean) -> Unit,
    onRelay3: (Boolean) -> Unit,
    onRelay4: (Boolean) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Relay Control",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        RelayCard(
            title = "Relay 1",
            state = relay1,
            onToggle = onRelay1
        )

        RelayCard(
            title = "Relay 2",
            state = relay2,
            onToggle = onRelay2
        )

        RelayCard(
            title = "Relay 3",
            state = relay3,
            onToggle = onRelay3
        )

        RelayCard(
            title = "Relay 4",
            state = relay4,
            onToggle = onRelay4
        )

    }

}

@Composable
private fun RelayCard(
    title: String,
    state: Boolean,
    onToggle: (Boolean) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (state) "ON" else "OFF"
                )

            }

            Switch(
                checked = state,
                onCheckedChange = onToggle
            )

        }

    }

}