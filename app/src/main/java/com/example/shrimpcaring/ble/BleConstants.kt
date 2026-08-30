package com.example.shrimpcaring.ble

import java.util.UUID

object BleConstants {

    const val DEVICE_NAME = "ShrimpCaring"

    val SERVICE_UUID =
        UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b")

    // WiFi
    val SSID_UUID =
        UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8")

    val PASSWORD_UUID =
        UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a9")

    val DEVICE_UUID =
        UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26ad")

    // Configuration
    val POND_UUID =
        UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26af")

    val INTERVAL_UUID =
        UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26ae")

    val SAVE_UUID =
        UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26ab")

    // Relay
    val RELAY_UUID =
        UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26b0")

    // Sensor
    val SENSOR_UUID =
        UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26b1")

}