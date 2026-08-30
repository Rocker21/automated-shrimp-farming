package com.example.shrimpcaring.ble

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

class BlePermission(
    private val context: Context
) {

    fun hasPermission(): Boolean {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S)
            return true

        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH_SCAN
        ) == PackageManager.PERMISSION_GRANTED &&

                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
    }

}