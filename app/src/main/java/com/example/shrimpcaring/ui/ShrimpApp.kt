package com.example.shrimpcaring.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shrimpcaring.navigation.AppNavigation
import com.example.shrimpcaring.navigation.BottomNavigationBar
import com.example.shrimpcaring.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShrimpApp() {

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry?.destination?.route

    // Find the title for the current route
    val title = when {
        currentRoute == Screen.Home.route -> Screen.Home.title
        currentRoute == Screen.DeviceSelection.route -> Screen.DeviceSelection.title
        currentRoute == Screen.AeratorCount.route -> Screen.AeratorCount.title
        currentRoute == Screen.Aerator.route -> Screen.Aerator.title
        currentRoute == Screen.Ph.route -> Screen.Ph.title
        currentRoute == Screen.Do.route -> Screen.Do.title
        currentRoute == Screen.Temperature.route -> Screen.Temperature.title
        currentRoute == Screen.Dashboard.route -> Screen.Dashboard.title
        currentRoute == Screen.Control.route -> Screen.Control.title
        currentRoute == Screen.Logger.route -> Screen.Logger.title
        currentRoute == Screen.History.route -> Screen.History.title
        currentRoute == Screen.Setup.route -> Screen.Setup.title
        else -> "Shrimp Caring"
    }

    val canPop = navController.previousBackStackEntry != null

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    if (canPop) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        },

        bottomBar = {

            BottomNavigationBar(

                navController = navController,

                currentRoute = currentRoute

            )

        }

    ) { padding ->

        AppNavigation(

            navController = navController,

            modifier = androidx.compose.ui.Modifier
                .padding(padding)

        )

    }

}
