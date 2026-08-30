package com.example.shrimpcaring.screens.home

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shrimpcaring.models.Pond
import androidx.compose.foundation.ExperimentalFoundationApi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PondCard(
    pond: Pond,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = { /* Keep long click if needed, or remove */ }
            ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.Water,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = pond.name,
                    style = MaterialTheme.typography.titleLarge
                )

                Text("Location : ${pond.location}")

                Text(
                    text = if (pond.isOnline) "ONLINE" else "OFFLINE",
                    color = if (pond.isOnline)
                        Color(0xFF4CAF50)
                    else
                        Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )

            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Pond",
                    tint = Color.Red
                )
            }

        }

    }

}
