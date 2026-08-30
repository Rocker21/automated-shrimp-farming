package com.example.shrimpcaring.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StatusCard(
    title: String,
    status: String,
    online: Boolean
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = status,
                color =
                    if (online)
                        Color(0xFF4CAF50)
                    else
                        Color.Red,

                fontWeight = FontWeight.Bold
            )
        }
    }
}