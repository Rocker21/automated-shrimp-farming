package com.example.shrimpcaring

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.shrimpcaring.ui.ShrimpApp
import com.example.shrimpcaring.ui.theme.ShrimpCaringTheme
import com.example.shrimpcaring.viewmodel.ThemeViewModel

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }

        setContent {
            val isDarkTheme by ThemeViewModel.isDarkTheme.collectAsState()
            
            ShrimpCaringTheme(darkTheme = isDarkTheme) {
                ShrimpApp()
            }
        }
    }
}