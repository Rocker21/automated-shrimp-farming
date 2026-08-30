package com.example.shrimpcaring.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shrimpcaring.database.SensorEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    logs: List<SensorEntity>
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(logs) { log ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(dateFormat.format(Date(log.timestamp)))
                    Text("pH : ${log.ph}")
                    Text("Voltage : ${log.voltage}V")
                    Text("Current : ${log.current}A")
                    Text("Power : ${log.power}W")
                }
            }
        }
    }
}
