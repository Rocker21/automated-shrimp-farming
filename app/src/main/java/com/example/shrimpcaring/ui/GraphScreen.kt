package com.example.shrimpcaring.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GraphScreen() {

    var selectedGraph by remember {
        mutableStateOf("pH")
    }

    val graphs = listOf(
        "pH",
        "Voltage",
        "Current",
        "Power",
        "Energy",
        "Frequency",
        "Power Factor"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Graphs",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {

            items(graphs.size) { index ->

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    onClick = {

                        selectedGraph = graphs[index]

                    }
                ) {

                    Text(graphs[index])

                }

            }

        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ) {

            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                Text(
                    modifier = Modifier.padding(20.dp),
                    text = "$selectedGraph Graph Coming Soon"
                )

            }

        }

    }

}