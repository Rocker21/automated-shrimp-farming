package com.example.shrimpcaring.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SensorCard(
    title: String,
    value: String,
    unit: String
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                fontSize = 16.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "$value $unit",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}