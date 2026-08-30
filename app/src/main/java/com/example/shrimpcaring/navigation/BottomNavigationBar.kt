package com.example.shrimpcaring.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentRoute: String?
) {

    NavigationBar {

        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home")
            },
            icon = {
                Icon(
                    Icons.Default.Water,
                    contentDescription = "Ponds"
                )
            },
            label = {
                Text("Ponds")
            }
        )

        NavigationBarItem(
            selected = currentRoute == "logger",
            onClick = {
                navController.navigate("logger")
            },
            icon = {
                Icon(
                    Icons.Default.Storage,
                    contentDescription = "Logger"
                )
            },
            label = {
                Text("Logger")
            }
        )

        NavigationBarItem(
            selected = currentRoute == "history",
            onClick = {
                navController.navigate("history")
            },
            icon = {
                Icon(
                    Icons.Default.History,
                    contentDescription = "History"
                )
            },
            label = {
                Text("History")
            }
        )

        NavigationBarItem(
            selected = currentRoute == "setup",
            onClick = {
                navController.navigate("setup")
            },
            icon = {
                Icon(
                    Icons.Default.Tune,
                    contentDescription = "Setup"
                )
            },
            label = {
                Text("Setup")
            }
        )

        NavigationBarItem(
            selected = currentRoute == "settings",
            onClick = {
                navController.navigate("settings")
            },
            icon = {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            },
            label = {
                Text("Settings")
            }
        )

    }

}