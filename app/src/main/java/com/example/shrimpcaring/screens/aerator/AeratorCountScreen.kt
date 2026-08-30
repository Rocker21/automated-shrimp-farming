package com.example.shrimpcaring.screens.aerator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shrimpcaring.navigation.Screen
import com.example.shrimpcaring.viewmodel.PondViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AeratorCountScreen(
    navController: NavController,
    pondId: Int,
    pondViewModel: PondViewModel = viewModel()
) {
    val currentPond by pondViewModel.currentPond.collectAsState()

    var count by remember {
        mutableIntStateOf(1)
    }

    LaunchedEffect(pondId) {
        pondViewModel.loadPond(pondId)
    }

    // Update count when pond is loaded
    LaunchedEffect(currentPond) {
        currentPond?.let {
            if (it.aeratorCount > 0) {
                count = it.aeratorCount
            }
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            Text(
                "How many aerators are installed?",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(20.dp))

            (1..8).forEach { number ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = count == number,
                            onClick = { count = number }
                        )
                        .padding(vertical = 8.dp)
                ) {
                    RadioButton(
                        selected = count == number,
                        onClick = { count = number }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("$number Aerators")
                }
            }

            Spacer(Modifier.height(30.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    pondViewModel.updateAeratorCount(pondId, count)
                    navController.navigate(Screen.Aerator.createRoute(pondId)) {
                        // Pop the selection screen so back goes to selection correctly
                        popUpTo(Screen.DeviceSelection.route) { inclusive = false }
                    }
                }
            ) {
                Text("Continue")
            }
        }
    }
}
