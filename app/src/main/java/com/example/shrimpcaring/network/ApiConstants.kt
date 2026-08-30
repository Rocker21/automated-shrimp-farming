package com.example.shrimpcaring.network

object ApiConstants {

    const val BASE_URL =
        "https://api.shrimpcaring.in"

    const val API_BASE =
        "$BASE_URL/api"

    const val STATUS =
        "$API_BASE/status"

    const val PONDS =
        "$API_BASE/ponds"

    fun pondDevices(
        pondId: Int
    ): String {
        return "$PONDS/$pondId/devices"
    }

    fun pondAerators(
        pondId: Int
    ): String {
        return "$PONDS/$pondId/aerators"
    }

    fun pondSensors(
        pondId: Int
    ): String {
        return "$PONDS/$pondId/sensors"
    }

    const val DEVICES =
        "$API_BASE/devices"

    fun deviceAerators(
        deviceId: Int
    ): String {
        return "$DEVICES/$deviceId/aerators"
    }

    fun deviceSensors(
        deviceId: Int
    ): String {
        return "$DEVICES/$deviceId/sensors"
    }

    fun aeratorControl(
        aeratorId: Int
    ): String {
        return "$API_BASE/aerators/$aeratorId/control"
    }
}