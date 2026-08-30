package com.example.shrimpcaring.ui

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.shrimpcaring.service.LoggerService

@Composable
fun DataLoggerScreen(
    pondId: Int,
    recordCount: Int,
    isRecording: Boolean,
    onExport: () -> Unit,
    onDelete: () -> Unit
) {

    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }

    var interval by remember { mutableStateOf("10 sec") }

    val intervals = listOf(
        "5 sec",
        "10 sec",
        "30 sec",
        "1 min",
        "5 min"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            text = "Data Logger",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = if (isRecording)
                "Status : Recording"
            else
                "Status : Stopped"
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Stored Records : $recordCount"
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                expanded = true
            }
        ) {
            Text("Interval : $interval")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {

            intervals.forEach { item ->

                DropdownMenuItem(
                    text = {
                        Text(item)
                    },
                    onClick = {

                        interval = item
                        expanded = false

                    }
                )

            }

        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !isRecording,
            onClick = {

                val intent = Intent(
                    context,
                    LoggerService::class.java
                )

                intent.putExtra(
                    "interval",
                    interval
                )

                intent.putExtra(
                    "pondId",
                    pondId
                )

                ContextCompat.startForegroundService(
                    context,
                    intent
                )

            }
        ) {

            Text("START RECORDING")

        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            enabled = isRecording,
            onClick = {

                context.stopService(
                    Intent(
                        context,
                        LoggerService::class.java
                    )
                )

            }
        ) {

            Text("STOP RECORDING")

        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onExport
        ) {

            Text("EXPORT TO EXCEL")

        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onDelete
        ) {

            Text("DELETE LOGS")

        }

    }

}