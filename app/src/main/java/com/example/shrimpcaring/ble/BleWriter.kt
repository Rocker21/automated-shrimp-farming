package com.example.shrimpcaring.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothStatusCodes
import android.os.Build
import java.util.UUID

class BleWriter(

    private val connection: BleConnection

) {

    @SuppressLint("MissingPermission")
    private fun write(

        uuid: UUID,

        value: String

    ): Boolean {

        val gatt =
            connection.gatt ?: return false

        val service =
            connection.service ?: return false

        val characteristic =
            service.getCharacteristic(uuid) ?: return false

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            gatt.writeCharacteristic(

                characteristic,

                value.toByteArray(),

                BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT

            ) == BluetoothStatusCodes.SUCCESS

        } else {

            @Suppress("DEPRECATION")

            characteristic.value = value.toByteArray()

            characteristic.writeType =
                BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT

            gatt.writeCharacteristic(characteristic)

        }

    }

    // WiFi

    fun sendSSID(ssid: String) =
        write(BleConstants.SSID_UUID, ssid)

    fun sendPassword(password: String) =
        write(BleConstants.PASSWORD_UUID, password)

    fun sendDeviceName(name: String) =
        write(BleConstants.DEVICE_UUID, name)

    // Configuration

    fun sendPondId(id: Int) =
        write(BleConstants.POND_UUID, id.toString())

    fun sendSamplingInterval(interval: Int) =
        write(BleConstants.INTERVAL_UUID, interval.toString())

    fun saveConfiguration() =
        write(BleConstants.SAVE_UUID, "SAVE")

    // Relay

    fun sendRelayCommand(

        relay: Int,

        state: Boolean

    ) = write(

        BleConstants.RELAY_UUID,

        "$relay:${if (state) 1 else 0}"

    )

    // Sensor

    fun requestSensorData() =
        write(BleConstants.SENSOR_UUID, "READ")

}