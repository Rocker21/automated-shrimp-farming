package com.example.shrimpcaring.screens.aerator

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shrimpcaring.viewmodel.AeratorViewModel
import com.example.shrimpcaring.viewmodel.MainViewModel

@Composable
fun AeratorScreen(
    pondId: Int,
    viewModel: AeratorViewModel = viewModel(),
    mainViewModel: MainViewModel = viewModel()
) {

    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    val tabs = listOf(
        "Setup",
        "Control"
    )

    androidx.compose.foundation.layout.Column(
        modifier = Modifier.fillMaxSize()
    ) {

        TabRow(
            selectedTabIndex = selectedTab
        ) {

            tabs.forEachIndexed { index, title ->

                Tab(
                    selected = selectedTab == index,
                    onClick = {
                        selectedTab = index
                    },
                    text = {
                        Text(title)
                    }
                )

            }

        }

        when (selectedTab) {

            0 -> AeratorSetupTab(
                pondId = pondId,
                viewModel = viewModel,
                mainViewModel = mainViewModel
            )

            1 -> AeratorControlTab(
                pondId = pondId,
                mainViewModel = mainViewModel,
                onGoToSetup = { selectedTab = 0 }
            )

        }

    }

}
