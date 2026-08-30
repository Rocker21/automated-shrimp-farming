package com.example.shrimpcaring.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Handler
import android.os.Looper


class BleScanner(
    context: Context
) {

    private val appContext =
        context.applicationContext


    // =====================================================
    // PERMISSION
    // =====================================================

    private val permission =
        BlePermission(appContext)


    // =====================================================
    // BLUETOOTH
    // =====================================================

    private val bluetoothManager =
        appContext.getSystemService(
            Context.BLUETOOTH_SERVICE
        ) as BluetoothManager


    private val bluetoothAdapter:
            BluetoothAdapter =
        bluetoothManager.adapter


    private val scanner:
            BluetoothLeScanner?
        get() = bluetoothAdapter.bluetoothLeScanner


    // =====================================================
    // CALLBACKS
    // =====================================================

    var onDeviceFound:
            ((BluetoothDevice) -> Unit)? =
        null


    var onScanFinished:
            (() -> Unit)? =
        null


    var onError:
            ((String) -> Unit)? =
        null


    // =====================================================
    // SCAN STATE
    // =====================================================

    private var scanning =
        false


    private val handler =
        Handler(
            Looper.getMainLooper()
        )


    // Prevent the same device from being
    // reported repeatedly.

    private val discoveredDevices =
        mutableSetOf<String>()


    // =====================================================
    // START SCAN
    // =====================================================

    @SuppressLint("MissingPermission")
    fun startScan() {

        // ---------------------------------------------
        // Permission
        // ---------------------------------------------

        if (
            !permission.hasPermission()
        ) {

            onError?.invoke(
                "Bluetooth permission denied"
            )

            return
        }


        // ---------------------------------------------
        // Bluetooth enabled?
        // ---------------------------------------------

        if (
            !bluetoothAdapter.isEnabled
        ) {

            onError?.invoke(
                "Bluetooth is turned off"
            )

            return
        }


        // ---------------------------------------------
        // Scanner available?
        // ---------------------------------------------

        val bleScanner =
            scanner


        if (
            bleScanner == null
        ) {

            onError?.invoke(
                "BLE scanner unavailable"
            )

            return
        }


        // ---------------------------------------------
        // Stop previous scan
        // ---------------------------------------------

        if (scanning) {

            stopScan()
        }


        // ---------------------------------------------
        // Clear previous devices
        // ---------------------------------------------

        discoveredDevices.clear()


        // ---------------------------------------------
        // Scan settings
        // ---------------------------------------------

        val settings =
            ScanSettings.Builder()
                .setScanMode(
                    ScanSettings.SCAN_MODE_LOW_LATENCY
                )
                .build()


        // ---------------------------------------------
        // IMPORTANT:
        //
        // NO ScanFilter
        //
        // We scan all BLE devices and filter
        // SHRIMP-CARING-BT ourselves.
        // ---------------------------------------------

        try {

            bleScanner.startScan(
                null,
                settings,
                scanCallback
            )


            scanning =
                true


        } catch (
            e: Exception
        ) {

            scanning =
                false


            onError?.invoke(
                "BLE scan failed: ${e.message}"
            )


            return
        }


        // ---------------------------------------------
        // Automatically stop after 10 seconds
        // ---------------------------------------------

        handler.postDelayed({

            stopScan()

        }, 10000)
    }


    // =====================================================
    // STOP SCAN
    // =====================================================

    @SuppressLint("MissingPermission")
    fun stopScan() {

        handler.removeCallbacksAndMessages(
            null
        )


        if (!scanning) {

            return
        }


        val bleScanner =
            scanner


        if (
            bleScanner != null &&
            permission.hasPermission()
        ) {

            try {

                bleScanner.stopScan(
                    scanCallback
                )

            } catch (
                e: Exception
            ) {

                // Ignore stop errors

            }
        }


        scanning =
            false


        onScanFinished?.invoke()
    }


    // =====================================================
    // SCAN CALLBACK
    // =====================================================

    private val scanCallback =
        object : ScanCallback() {


            @SuppressLint("MissingPermission")
            override fun onScanResult(
                callbackType: Int,
                result: ScanResult
            ) {

                val device =
                    result.device
                        ?: return


                // -----------------------------------------
                // Device address
                // -----------------------------------------

                val address =
                    device.address
                        ?: return


                // -----------------------------------------
                // Avoid duplicates
                // -----------------------------------------

                if (
                    discoveredDevices.contains(
                        address
                    )
                ) {

                    return
                }


                // -----------------------------------------
                // Get advertised name
                // -----------------------------------------

                val advertisedName =
                    result.scanRecord
                        ?.deviceName


                // -----------------------------------------
                // Device name
                // -----------------------------------------

                val deviceName =
                    try {

                        device.name

                    } catch (
                        e: SecurityException
                    ) {

                        null
                    }


                // -----------------------------------------
                // DEBUG
                // -----------------------------------------

                android.util.Log.d(
                    "BleScanner",
                    "BLE device found: " +
                            "name=$deviceName, " +
                            "advertisedName=$advertisedName, " +
                            "address=$address"
                )


                // -----------------------------------------
                // Target device filtering
                // -----------------------------------------

                // Check by Service UUID in scan record
                val serviceUuids = result.scanRecord?.serviceUuids
                val matchesUuid = serviceUuids?.any { it.uuid == BleConstants.SERVICE_UUID } ?: false

                // Check by Name - allow partial matches for "Shrimp" or "Caring"
                val targetName = BleConstants.DEVICE_NAME
                val matchesTarget = deviceName?.contains(targetName, ignoreCase = true) == true || 
                        advertisedName?.contains(targetName, ignoreCase = true) == true ||
                        deviceName?.contains("Shrimp", ignoreCase = true) == true ||
                        advertisedName?.contains("Shrimp", ignoreCase = true) == true ||
                        deviceName?.contains("ESP32", ignoreCase = true) == true


                if (matchesUuid || matchesTarget) {
                    discoveredDevices.add(address)
                    android.util.Log.d("BleScanner", "SHRIMP ESP32 FOUND: $address (Matches UUID=$matchesUuid, Name=$matchesTarget)")
                    onDeviceFound?.invoke(device)
                }
            }


            override fun onScanFailed(
                errorCode: Int
            ) {

                scanning =
                    false


                val message =
                    when (
                        errorCode
                    ) {

                        SCAN_FAILED_ALREADY_STARTED ->
                            "BLE scan already started"

                        SCAN_FAILED_APPLICATION_REGISTRATION_FAILED ->
                            "BLE application registration failed"

                        SCAN_FAILED_INTERNAL_ERROR ->
                            "BLE internal error"

                        SCAN_FAILED_FEATURE_UNSUPPORTED ->
                            "BLE scanning unsupported"

                        else ->
                            "BLE scan failed: $errorCode"
                    }


                android.util.Log.e(
                    "BleScanner",
                    message
                )


                onError?.invoke(
                    message
                )
            }
        }
}