package com.example.shrimpcaring.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context

class BleManager(
    context: Context
) {

    // Core Modules
    val scanner = BleScanner(context)

    val connection = BleConnection(context)

    val writer = BleWriter(connection)

    val provision = BleProvision(writer)

    val relay = RelayManager(writer)

    val sensor = SensorManager(writer)

    val notification = BleNotification(
        connection,
        sensor
    )

    var lastSensorData: SensorData? = null
        private set

    init {

        connection.addListener(object : BleConnection.BleListener {
            override fun onNotification(characteristic: BluetoothGattCharacteristic, value: ByteArray) {
                notification.handleNotification(characteristic, value)
            }
        })

        // Keep a simple global listener for background tasks (e.g. LoggerService)
        sensor.onSensorDataReceived = { data ->
            lastSensorData = data
        }

    }

    //-------------------------
    // Scanner
    //-------------------------

    fun startScan() =
        scanner.startScan()

    fun stopScan() =
        scanner.stopScan()

    //-------------------------
    // Connection
    //-------------------------

    fun connect(device: BluetoothDevice) =
        connection.connect(device)

    fun disconnect() =
        connection.disconnect()

    //-------------------------
    // Provision
    //-------------------------

    fun provisionESP32(

        pondId: Int,

        ssid: String,

        password: String,

        deviceName: String,

        interval: Int

    ) {

        provision.provision(

            pondId,

            ssid,

            password,

            deviceName,

            interval

        )

    }

    //-------------------------
    // Relay
    //-------------------------

    fun relayOn(relayNumber: Int) =
        relay.turnOn(relayNumber)

    fun relayOff(relayNumber: Int) =
        relay.turnOff(relayNumber)

    fun relayToggle(

        relayNumber: Int,

        currentState: Boolean

    ) = relay.toggle(
        relayNumber,
        currentState
    )

    //-------------------------
    // Sensors
    //-------------------------

    fun requestSensorData() =
        sensor.requestData()

    fun readSensorData() =
        connection.readCharacteristic(BleConstants.SENSOR_UUID)

    fun enableNotifications() =
        notification.enableNotifications()

    fun disableNotifications() =
        notification.disableNotifications()

}
