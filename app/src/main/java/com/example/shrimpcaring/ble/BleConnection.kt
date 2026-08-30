package com.example.shrimpcaring.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothProfile
import android.content.Context
import java.util.UUID

class BleConnection(
    private val context: Context
) {

    private val permission = BlePermission(context)

    var gatt: BluetoothGatt? = null
        private set

    var service: BluetoothGattService? = null
        private set

    var isConnected = false
        private set

    private val listeners = mutableListOf<BleListener>()

    interface BleListener {
        fun onConnected() {}
        fun onDisconnected() {}
        fun onServiceReady() {}
        fun onError(message: String) {}
        fun onNotification(characteristic: BluetoothGattCharacteristic, value: ByteArray) {}
    }

    fun addListener(listener: BleListener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
            // If already connected, notify immediately
            if (isConnected) listener.onConnected()
            if (service != null) listener.onServiceReady()
        }
    }

    fun removeListener(listener: BleListener) {
        listeners.remove(listener)
    }

    @SuppressLint("MissingPermission")
    fun connect(device: BluetoothDevice) {

        if (!permission.hasPermission()) {
            listeners.forEach { it.onError("Bluetooth permission denied") }
            return
        }

        gatt = device.connectGatt(
            context,
            false,
            callback
        )
    }

    @SuppressLint("MissingPermission")
    fun disconnect() {

        gatt?.disconnect()
        gatt?.close()

        gatt = null
        service = null
        isConnected = false
    }

    @SuppressLint("MissingPermission")
    fun readCharacteristic(uuid: UUID): Boolean {
        val gatt = this.gatt ?: return false
        val service = this.service ?: return false
        val characteristic = service.getCharacteristic(uuid) ?: return false
        return gatt.readCharacteristic(characteristic)
    }

    private val callback = object : BluetoothGattCallback() {

        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(
            gatt: BluetoothGatt,
            status: Int,
            newState: Int
        ) {

            when (newState) {

                BluetoothProfile.STATE_CONNECTED -> {

                    isConnected = true
                    this@BleConnection.gatt = gatt

                    listeners.forEach { it.onConnected() }

                    // Request higher MTU for long sensor data strings
                    gatt.requestMtu(512)
                }

                BluetoothProfile.STATE_DISCONNECTED -> {

                    isConnected = false
                    service = null

                    listeners.forEach { it.onDisconnected() }
                }
            }
        }

        @SuppressLint("MissingPermission")
        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
            println("BLE MTU CHANGED: $mtu, status: $status")
            gatt.discoverServices()
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(
            gatt: BluetoothGatt,
            status: Int
        ) {

            service =
                gatt.getService(BleConstants.SERVICE_UUID)

            if (service == null) {
                println("BLE ERROR: Service ${BleConstants.SERVICE_UUID} not found")
                listeners.forEach { it.onError("Service not found") }
                return
            }

            println("BLE SUCCESS: Service ready. Found ${service?.characteristics?.size ?: 0} characteristics")
            listeners.forEach { it.onServiceReady() }
        }

        // Android 13+ Callback
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            println("BLE NOTIFY [${characteristic.uuid}]: ${String(value)}")
            listeners.forEach { it.onNotification(characteristic, value) }
        }

        // Legacy Callback
        @Suppress("DEPRECATION")
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            val value = characteristic.value ?: byteArrayOf()
            println("BLE NOTIFY LEGACY [${characteristic.uuid}]: ${String(value)}")
            listeners.forEach { it.onNotification(characteristic, value) }
        }

        // Support for READ responses
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                println("BLE READ [${characteristic.uuid}]: ${String(value)}")
                listeners.forEach { it.onNotification(characteristic, value) }
            }
        }

        @Suppress("DEPRECATION")
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val value = characteristic.value ?: byteArrayOf()
                println("BLE READ LEGACY [${characteristic.uuid}]: ${String(value)}")
                listeners.forEach { it.onNotification(characteristic, value) }
            }
        }
    }

}
