package com.example.shrimpcaring.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic

class BleNotification(

    private val connection: BleConnection,

    private val sensorManager: SensorManager

) {

    //--------------------------------------------------------
    // Enable Notifications
    //--------------------------------------------------------

    @SuppressLint("MissingPermission")
    fun enableNotifications(): Boolean {

        val gatt =
            connection.gatt ?: return false

        val service =
            connection.service ?: return false

        val characteristic =
            service.getCharacteristic(
                BleConstants.SENSOR_UUID
            ) ?: return false

        gatt.setCharacteristicNotification(
            characteristic,
            true
        )

        // Write to CCCD to enable notifications on the peripheral
        val descriptor = characteristic.getDescriptor(
            java.util.UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
        )
        if (descriptor != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                gatt.writeDescriptor(
                    descriptor,
                    android.bluetooth.BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                )
            } else {
                @Suppress("DEPRECATION")
                descriptor.value = android.bluetooth.BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                gatt.writeDescriptor(descriptor)
            }
        }

        return true

    }

    //--------------------------------------------------------
    // Disable Notifications
    //--------------------------------------------------------

    @SuppressLint("MissingPermission")
    fun disableNotifications(): Boolean {

        val gatt =
            connection.gatt ?: return false

        val service =
            connection.service ?: return false

        val characteristic =
            service.getCharacteristic(
                BleConstants.SENSOR_UUID
            ) ?: return false

        return gatt.setCharacteristicNotification(
            characteristic,
            false
        )

    }

    //--------------------------------------------------------
    // Process Incoming Notification
    //--------------------------------------------------------

    fun handleNotification(
        characteristic: BluetoothGattCharacteristic,
        value: ByteArray
    ) {

        if (
            characteristic.uuid ==
            BleConstants.SENSOR_UUID
        ) {

            val data = String(value)

            sensorManager.parseSensorData(data)

        }

    }

}
