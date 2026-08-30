package com.example.shrimpcaring.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.shrimpcaring.screens.aerator.AeratorCountScreen
import com.example.shrimpcaring.screens.aerator.AeratorScreen
import com.example.shrimpcaring.screens.devices.DeviceSelectionScreen
import com.example.shrimpcaring.screens.home.HomeScreen
import com.example.shrimpcaring.screens.ph.PhScreen
import com.example.shrimpcaring.ui.ControlScreen
import com.example.shrimpcaring.ui.DashboardScreen
import com.example.shrimpcaring.ui.DataLoggerScreen
import com.example.shrimpcaring.ui.HistoryScreen
import com.example.shrimpcaring.ui.SetupScreen
import com.example.shrimpcaring.ui.SettingsScreen
import com.example.shrimpcaring.viewmodel.MainViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Toast
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val saveLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        uri?.let {
            scope.launch {
                val csvContent = mainViewModel.getLogsCsvContent()
                if (csvContent.isNotEmpty()) {
                    try {
                        context.contentResolver.openOutputStream(it)?.use { output ->
                            output.write(csvContent.toByteArray())
                        }
                        Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error saving file", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {

        //--------------------------------------------------
        // HOME
        //--------------------------------------------------
        composable(Screen.Home.route) {

            HomeScreen(
                navController = navController
            )

        }

        //--------------------------------------------------
        // DEVICE SELECTION
        //--------------------------------------------------
        composable(
            route = Screen.DeviceSelection.route,
            arguments = listOf(
                navArgument("pondId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val pondId =
                backStackEntry.arguments?.getInt("pondId") ?: 0

            DeviceSelectionScreen(
                navController = navController,
                pondId = pondId
            )

        }

        //--------------------------------------------------
        // AERATOR COUNT
        //--------------------------------------------------
        composable(
            route = Screen.AeratorCount.route,
            arguments = listOf(
                navArgument("pondId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val pondId =
                backStackEntry.arguments?.getInt("pondId") ?: 0

            AeratorCountScreen(
                navController = navController,
                pondId = pondId
            )

        }

        //--------------------------------------------------
        // AERATOR MODULE
        //--------------------------------------------------
        composable(
            route = Screen.Aerator.route,
            arguments = listOf(
                navArgument("pondId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val pondId =
                backStackEntry.arguments?.getInt("pondId") ?: 0

            AeratorScreen(
                pondId = pondId,
                mainViewModel = mainViewModel
            )

        }

        //--------------------------------------------------
        // pH MODULE
        //--------------------------------------------------
        composable(
            route = Screen.Ph.route,
            arguments = listOf(
                navArgument("pondId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val pondId =
                backStackEntry.arguments?.getInt("pondId") ?: 0

            PhScreen(
                pondId = pondId
            )

        }

        //--------------------------------------------------
        // DASHBOARD
        //--------------------------------------------------
        composable(Screen.Dashboard.route) {
            val ph by mainViewModel.ph.collectAsState()
            val voltage by mainViewModel.voltage.collectAsState()
            val current by mainViewModel.current.collectAsState()
            val power by mainViewModel.power.collectAsState()
            val energy by mainViewModel.energy.collectAsState()
            val frequency by mainViewModel.frequency.collectAsState()
            val pf by mainViewModel.powerFactor.collectAsState()
            val online by mainViewModel.wifiConnected.collectAsState()

            DashboardScreen(
                ph = ph.toString(),
                voltage = voltage.toString(),
                current = current.toString(),
                power = power.toString(),
                energy = energy.toString(),
                frequency = frequency.toString(),
                pf = pf.toString(),
                online = online
            )
        }

        //--------------------------------------------------
        // CONTROL
        //--------------------------------------------------
        composable(Screen.Control.route) {
            val r1 by mainViewModel.relay1.collectAsState()
            val r2 by mainViewModel.relay2.collectAsState()
            val r3 by mainViewModel.relay3.collectAsState()
            val r4 by mainViewModel.relay4.collectAsState()

            ControlScreen(
                relay1 = r1,
                relay2 = r2,
                relay3 = r3,
                relay4 = r4,
                onRelay1 = { mainViewModel.setRelay1(it) },
                onRelay2 = { mainViewModel.setRelay2(it) },
                onRelay3 = { mainViewModel.setRelay3(it) },
                onRelay4 = { mainViewModel.setRelay4(it) }
            )
        }

        //--------------------------------------------------
        // LOGGER
        //--------------------------------------------------
        composable(Screen.Logger.route) {
            val count by mainViewModel.recordCount.collectAsState()
            val recording by mainViewModel.recording.collectAsState()
            val activePondId by mainViewModel.activePondId.collectAsState()

            DataLoggerScreen(
                pondId = activePondId ?: 1,
                recordCount = count,
                isRecording = recording,
                onExport = {
                    val fileName = "ShrimpCaring_Logs_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.csv"
                    saveLauncher.launch(fileName)
                },
                onDelete = { mainViewModel.clearLogs() }
            )
        }

        //--------------------------------------------------
        // HISTORY
        //--------------------------------------------------
        composable(Screen.History.route) {
            val logs by mainViewModel.logs.collectAsState(initial = emptyList())
            HistoryScreen(logs = logs)
        }

        //--------------------------------------------------
        // SETUP
        //--------------------------------------------------
        composable(Screen.Setup.route) {
            SetupScreen(
                onBluetooth = { /* TODO */ },
                onWifi = { _, _ -> /* TODO */ }
            )
        }

        //--------------------------------------------------
        // SETTINGS
        //--------------------------------------------------
        composable(Screen.Settings.route) {
            SettingsScreen()
        }

    }

}
