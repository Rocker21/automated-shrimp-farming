package com.example.shrimpcaring.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothDevice
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shrimpcaring.ble.BleManager
import com.example.shrimpcaring.di.ServiceLocator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.shrimpcaring.ble.BleConnection

class AeratorViewModel(application: Application) : AndroidViewModel(application) {

    private val bleManager: BleManager = ServiceLocator.provideBleManager(application)

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _foundDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val foundDevices: StateFlow<List<BluetoothDevice>> = _foundDevices.asStateFlow()

    private val _connectedDevice = MutableStateFlow<BluetoothDevice?>(null)
    val connectedDevice: StateFlow<BluetoothDevice?> = _connectedDevice.asStateFlow()

    private val _statusMessage = MutableStateFlow("")
    val statusMessage: StateFlow<String> = _statusMessage.asStateFlow()

    init {
        bleManager.scanner.onDeviceFound = { device ->
            val currentList = _foundDevices.value
            if (!currentList.contains(device)) {
                _foundDevices.value = currentList + device
            }
        }

        bleManager.scanner.onScanFinished = {
            _isScanning.value = false
            if (_foundDevices.value.isEmpty()) {
                _statusMessage.value = "No ESP32 devices found"
            } else {
                _statusMessage.value = "Scan finished. Found ${_foundDevices.value.size} devices."
            }
        }

        bleManager.scanner.onError = { error ->
            _isScanning.value = false
            _statusMessage.value = "Error: $error"
        }

        // Sync initial connection state
        if (bleManager.connection.isConnected) {
            _connectedDevice.value = bleManager.connection.gatt?.device
        }

        // Listen for global connection changes
        bleManager.connection.addListener(object : BleConnection.BleListener {
            override fun onConnected() {
                _connectedDevice.value = bleManager.connection.gatt?.device
            }

            override fun onDisconnected() {
                _connectedDevice.value = null
            }
        })
    }

    @SuppressLint("MissingPermission")
    fun disconnect() {
        viewModelScope.launch {
            bleManager.disconnect()
            _connectedDevice.value = null
            _statusMessage.value = "Disconnected"
        }
    }

    fun startScan() {
        viewModelScope.launch {
            _foundDevices.value = emptyList()
            _isScanning.value = true
            _statusMessage.value = "Scanning for ESP32..."
            bleManager.startScan()
        }
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(device: BluetoothDevice) {
        viewModelScope.launch {
            _statusMessage.value = "Connecting to ${device.name ?: "Unknown"}..."
            bleManager.connect(device)
            _connectedDevice.value = device
            _statusMessage.value = "Connected to ${device.name ?: "Unknown"}"
        }
    }

    fun saveConfiguration(
        pondId: Int,
        ssid: String,
        password: String,
        deviceName: String,
        intervalString: String
    ) {
        val intervalInt = when (intervalString) {
            "5 sec" -> 5
            "10 sec" -> 10
            "30 sec" -> 30
            "1 min" -> 60
            "5 min" -> 300
            else -> 10
        }

        viewModelScope.launch {
            // 1. Save locally in Room
            val config = com.example.shrimpcaring.database.Configuration(
                pondId = pondId,
                wifiSSID = ssid,
                wifiPassword = password,
                samplingInterval = intervalInt
            )
            ServiceLocator.provideDatabase(getApplication()).configurationDao().insert(config)
            
            // 2. Trigger BLE provisioning to ESP32
            _statusMessage.value = "Sending config to device..."
            
            bleManager.provisionESP32(
                pondId = pondId,
                ssid = ssid,
                password = password,
                deviceName = deviceName,
                interval = intervalInt
            )
            
            _statusMessage.value = "Configuration Sent via Bluetooth"
        }
    }
}
