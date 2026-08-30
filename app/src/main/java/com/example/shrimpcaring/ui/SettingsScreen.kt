package com.example.shrimpcaring.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shrimpcaring.viewmodel.ThemeViewModel

@Composable
fun SettingsScreen(
    themeViewModel: ThemeViewModel = viewModel()
) {
    val isDarkTheme by ThemeViewModel.isDarkTheme.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Dark Theme",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = if (isDarkTheme) "Enabled" else "Disabled",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { themeViewModel.toggleTheme(it) }
                )
            }
        }
    }
}
