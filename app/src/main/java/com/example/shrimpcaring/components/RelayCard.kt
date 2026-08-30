package com.example.shrimpcaring.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RelayCard(
    title: String,
    gpio: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    fontSize = 20.sp
                )

                Text(
                    text = gpio,
                    fontSize = 13.sp
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}