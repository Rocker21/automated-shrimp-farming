package com.example.shrimpcaring.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddPondDialog(
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {

    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {

            Text("Add Pond")

        },

        text = {

            Column {

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    label = {
                        Text("Pond Name")
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = location,
                    onValueChange = {
                        location = it
                    },
                    label = {
                        Text("Location")
                    }
                )

            }

        },

        confirmButton = {

            Button(
                onClick = {

                    onSave(name, location)

                }
            ) {

                Text("Save")

            }

        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("Cancel")

            }

        }

    )

}