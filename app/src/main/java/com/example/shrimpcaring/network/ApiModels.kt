package com.example.shrimpcaring.network

// =====================================================
// SERVER STATUS
// =====================================================

data class ServerStatus(
    val online: Boolean,
    val message: String
)


// =====================================================
// POND
// =====================================================

data class ApiPond(
    val id: Int,
    val name: String,
    val location: String
)


// =====================================================
// DEVICE
// =====================================================

data class ApiDevice(
    val id: Int,
    val pondId: Int,
    val deviceName: String,
    val deviceType: String,
    val ipAddress: String,
    val online: Boolean,
    val createdAt: String? = null
)


// =====================================================
// AERATOR
// =====================================================

data class Aerator(
    val id: Int,
    val pond_id: Int,
    val device_id: Int,
    val name: String,
    val relay_number: Int,
    val state: Int,
    val created_at: String? = null
)


// =====================================================
// ESP32 / PZEM ELECTRICAL DATA
// =====================================================

data class Esp32SensorData(
    val success: Boolean,
    val device: String,
    val pondId: Int,
    val pzemConnected: Boolean,

    val voltage: Double,
    val current: Double,
    val power: Double,
    val energy: Double,
    val frequency: Double,
    val powerFactor: Double
)


// =====================================================
// AERATOR CONTROL RESPONSE
// =====================================================

data class AeratorControlResponse(
    val success: Boolean,
    val aerator_id: Int,
    val aerator_name: String?,
    val relay_number: Int,
    val state: Boolean,
    val device: String?,
    val esp32_ip: String?
)


// =====================================================
// DEVICE REGISTRATION RESPONSE
// =====================================================

data class DeviceRegistrationResponse(
    val success: Boolean,
    val device_id: Int,
    val device_name: String,
    val ip_address: String,
    val action: String
)