package com.example.shrimpcaring.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ShrimpApi {

    // =====================================================
    // API KEY
    // =====================================================

    private val apiKey =
        "cb198e7fe3f8a197513190adbe062b1c928529e042f143efa7ba023aeda04788"


    // =====================================================
    // CONNECTION
    // =====================================================

    private fun createConnection(
        urlString: String,
        method: String
    ): HttpURLConnection {

        val url = URL(urlString)

        val connection =
            url.openConnection() as HttpURLConnection

        connection.requestMethod = method

        connection.connectTimeout = 10000
        connection.readTimeout = 10000

        connection.setRequestProperty(
            "Accept",
            "application/json"
        )

        connection.setRequestProperty(
            "Content-Type",
            "application/json"
        )

        connection.setRequestProperty(
            "X-API-Key",
            apiKey
        )

        return connection
    }


    // =====================================================
    // SERVER STATUS
    // =====================================================

    suspend fun getServerStatus(): ServerStatus =
        withContext(Dispatchers.IO) {

            val connection =
                createConnection(
                    ApiConstants.STATUS,
                    "GET"
                )

            try {

                val responseCode =
                    connection.responseCode

                if (responseCode !in 200..299) {

                    throw Exception(
                        "Server status HTTP $responseCode"
                    )
                }

                val response =
                    connection.inputStream
                        .bufferedReader()
                        .use {
                            it.readText()
                        }

                val json =
                    JSONObject(response)

                ServerStatus(
                    online =
                        json.optBoolean(
                            "online",
                            false
                        ),

                    message =
                        json.optString(
                            "message",
                            ""
                        )
                )

            } finally {

                connection.disconnect()
            }
        }


    // =====================================================
    // GET PONDS
    // =====================================================

    suspend fun getPonds(): List<ApiPond> =
        withContext(Dispatchers.IO) {

            val connection =
                createConnection(
                    ApiConstants.PONDS,
                    "GET"
                )

            try {

                val responseCode =
                    connection.responseCode

                if (responseCode !in 200..299) {

                    val error =
                        connection.errorStream
                            ?.bufferedReader()
                            ?.use {
                                it.readText()
                            }

                    throw Exception(
                        "Get ponds failed: HTTP $responseCode $error"
                    )
                }

                val response =
                    connection.inputStream
                        .bufferedReader()
                        .use {
                            it.readText()
                        }

                val array =
                    JSONArray(response)

                val result =
                    mutableListOf<ApiPond>()

                for (
                i in 0 until array.length()
                ) {

                    val obj =
                        array.getJSONObject(i)

                    result.add(
                        ApiPond(
                            id =
                                obj.optInt(
                                    "id",
                                    0
                                ),

                            name =
                                obj.optString(
                                    "name",
                                    ""
                                ),

                            location =
                                obj.optString(
                                    "location",
                                    ""
                                )
                        )
                    )
                }

                result

            } finally {

                connection.disconnect()
            }
        }


    // =====================================================
    // CREATE POND
    // =====================================================

    suspend fun createPond(
        name: String,
        location: String
    ): ApiPond =
        withContext(Dispatchers.IO) {

            val connection =
                createConnection(
                    ApiConstants.PONDS,
                    "POST"
                )

            connection.doOutput = true

            val body =
                JSONObject().apply {

                    put(
                        "name",
                        name
                    )

                    put(
                        "location",
                        location
                    )
                }

            connection.outputStream
                .bufferedWriter()
                .use {
                    it.write(
                        body.toString()
                    )
                }

            try {

                val responseCode =
                    connection.responseCode

                if (responseCode !in 200..299) {

                    val error =
                        connection.errorStream
                            ?.bufferedReader()
                            ?.use {
                                it.readText()
                            }

                    throw Exception(
                        "Create pond failed: HTTP $responseCode $error"
                    )
                }

                val response =
                    connection.inputStream
                        .bufferedReader()
                        .use {
                            it.readText()
                        }

                val json =
                    JSONObject(response)

                ApiPond(
                    id =
                        json.optInt(
                            "pond_id",
                            json.optInt(
                                "id",
                                0
                            )
                        ),

                    name =
                        json.optString(
                            "name",
                            name
                        ),

                    location =
                        json.optString(
                            "location",
                            location
                        )
                )

            } finally {

                connection.disconnect()
            }
        }


    // =====================================================
    // DELETE POND
    // =====================================================

    suspend fun deletePond(
        pondId: Int
    ) =
        withContext(Dispatchers.IO) {

            val connection =
                createConnection(
                    "${ApiConstants.PONDS}/$pondId",
                    "DELETE"
                )

            try {

                val responseCode =
                    connection.responseCode

                if (responseCode !in 200..299) {

                    val error =
                        connection.errorStream
                            ?.bufferedReader()
                            ?.use {
                                it.readText()
                            }

                    throw Exception(
                        "Delete pond failed: HTTP $responseCode $error"
                    )
                }

            } finally {

                connection.disconnect()
            }
        }


    // =====================================================
    // GET POND AERATORS
    // =====================================================

    suspend fun getPondAerators(
        pondId: Int
    ): List<Aerator> =
        withContext(Dispatchers.IO) {

            val connection =
                createConnection(
                    ApiConstants.pondAerators(
                        pondId
                    ),
                    "GET"
                )

            try {

                val responseCode =
                    connection.responseCode

                if (responseCode !in 200..299) {

                    val error =
                        connection.errorStream
                            ?.bufferedReader()
                            ?.use {
                                it.readText()
                            }

                    throw Exception(
                        "Get aerators failed: HTTP $responseCode $error"
                    )
                }

                val response =
                    connection.inputStream
                        .bufferedReader()
                        .use {
                            it.readText()
                        }

                val array =
                    JSONArray(response)

                val result =
                    mutableListOf<Aerator>()

                for (
                i in 0 until array.length()
                ) {

                    val obj =
                        array.getJSONObject(i)

                    val relay =
                        obj.optInt(
                            "relay_number",
                            i + 1
                        )

                    result.add(
                        Aerator(
                            id =
                                obj.optInt(
                                    "id",
                                    0
                                ),

                            pond_id =
                                obj.optInt(
                                    "pond_id",
                                    pondId
                                ),

                            device_id =
                                obj.optInt(
                                    "device_id",
                                    0
                                ),

                            name =
                                obj.optString(
                                    "name",
                                    "Aerator $relay"
                                ),

                            relay_number =
                                relay,

                            state =
                                obj.optInt(
                                    "state",
                                    0
                                ),

                            created_at =
                                obj.optString(
                                    "created_at",
                                    null
                                )
                        )
                    )
                }

                result

            } finally {

                connection.disconnect()
            }
        }


    // =====================================================
    // GET DEVICE AERATORS
    // =====================================================

    suspend fun getDeviceAerators(
        deviceId: Int
    ): List<Aerator> =
        withContext(Dispatchers.IO) {

            val connection =
                createConnection(
                    ApiConstants.deviceAerators(
                        deviceId
                    ),
                    "GET"
                )

            try {

                val responseCode =
                    connection.responseCode

                if (responseCode !in 200..299) {

                    throw Exception(
                        "Get device aerators failed: HTTP $responseCode"
                    )
                }

                val response =
                    connection.inputStream
                        .bufferedReader()
                        .use {
                            it.readText()
                        }

                val array =
                    JSONArray(response)

                val result =
                    mutableListOf<Aerator>()

                for (
                i in 0 until array.length()
                ) {

                    val obj =
                        array.getJSONObject(i)

                    val relay =
                        obj.optInt(
                            "relay_number",
                            i + 1
                        )

                    result.add(
                        Aerator(
                            id =
                                obj.optInt(
                                    "id",
                                    0
                                ),

                            pond_id =
                                obj.optInt(
                                    "pond_id",
                                    0
                                ),

                            device_id =
                                deviceId,

                            name =
                                obj.optString(
                                    "name",
                                    "Aerator $relay"
                                ),

                            relay_number =
                                relay,

                            state =
                                obj.optInt(
                                    "state",
                                    0
                                ),

                            created_at =
                                obj.optString(
                                    "created_at",
                                    null
                                )
                        )
                    )
                }

                result

            } finally {

                connection.disconnect()
            }
        }


    // =====================================================
    // GET POND PZEM DATA
    // =====================================================

    suspend fun getPondSensors(
        pondId: Int
    ): Esp32SensorData =
        withContext(Dispatchers.IO) {

            println(
                "PZEM API: Requesting pond $pondId"
            )

            val connection =
                createConnection(
                    ApiConstants.pondSensors(
                        pondId
                    ),
                    "GET"
                )

            try {

                val responseCode =
                    connection.responseCode

                println(
                    "PZEM API: HTTP $responseCode"
                )

                if (responseCode !in 200..299) {

                    val error =
                        connection.errorStream
                            ?.bufferedReader()
                            ?.use {
                                it.readText()
                            }

                    throw Exception(
                        "PZEM API HTTP $responseCode: $error"
                    )
                }

                val response =
                    connection.inputStream
                        .bufferedReader()
                        .use {
                            it.readText()
                        }

                println(
                    "PZEM API RESPONSE: $response"
                )

                val root =
                    JSONObject(response)

                val relay1 =
                    root.optJSONObject(
                        "relay1"
                    )

                val source =
                    relay1 ?: root

                val powerFactor =
                    if (
                        source.has(
                            "power_factor"
                        )
                    ) {

                        source.optDouble(
                            "power_factor",
                            0.0
                        )

                    } else {

                        source.optDouble(
                            "powerFactor",
                            0.0
                        )
                    }

                Esp32SensorData(

                    success =
                        root.optBoolean(
                            "success",
                            false
                        ),

                    device =
                        root.optString(
                            "device_name",
                            root.optString(
                                "device",
                                ""
                            )
                        ),

                    pondId =
                        root.optInt(
                            "pond_id",
                            pondId
                        ),

                    pzemConnected =
                        root.optBoolean(
                            "pzem_connected",
                            false
                        ),

                    voltage =
                        source.optDouble(
                            "voltage",
                            0.0
                        ),

                    current =
                        source.optDouble(
                            "current",
                            0.0
                        ),

                    power =
                        source.optDouble(
                            "power",
                            0.0
                        ),

                    energy =
                        source.optDouble(
                            "energy",
                            0.0
                        ),

                    frequency =
                        source.optDouble(
                            "frequency",
                            0.0
                        ),

                    powerFactor =
                        powerFactor
                )

            } finally {

                connection.disconnect()
            }
        }


    // =====================================================
    // GET DEVICE PZEM DATA
    // =====================================================

    suspend fun getDeviceSensors(
        deviceId: Int
    ): Esp32SensorData =
        withContext(Dispatchers.IO) {

            val connection =
                createConnection(
                    ApiConstants.deviceSensors(
                        deviceId
                    ),
                    "GET"
                )

            try {

                val responseCode =
                    connection.responseCode

                if (responseCode !in 200..299) {

                    val error =
                        connection.errorStream
                            ?.bufferedReader()
                            ?.use {
                                it.readText()
                            }

                    throw Exception(
                        "Device sensor HTTP $responseCode: $error"
                    )
                }

                val response =
                    connection.inputStream
                        .bufferedReader()
                        .use {
                            it.readText()
                        }

                val root =
                    JSONObject(response)

                val relay1 =
                    root.optJSONObject(
                        "relay1"
                    )

                val source =
                    relay1 ?: root

                Esp32SensorData(

                    success =
                        root.optBoolean(
                            "success",
                            false
                        ),

                    device =
                        root.optString(
                            "device_name",
                            root.optString(
                                "device",
                                ""
                            )
                        ),

                    pondId =
                        root.optInt(
                            "pond_id",
                            0
                        ),

                    pzemConnected =
                        root.optBoolean(
                            "pzem_connected",
                            false
                        ),

                    voltage =
                        source.optDouble(
                            "voltage",
                            0.0
                        ),

                    current =
                        source.optDouble(
                            "current",
                            0.0
                        ),

                    power =
                        source.optDouble(
                            "power",
                            0.0
                        ),

                    energy =
                        source.optDouble(
                            "energy",
                            0.0
                        ),

                    frequency =
                        source.optDouble(
                            "frequency",
                            0.0
                        ),

                    powerFactor =
                        source.optDouble(
                            "power_factor",
                            source.optDouble(
                                "powerFactor",
                                0.0
                            )
                        )
                )

            } finally {

                connection.disconnect()
            }
        }


    // =====================================================
    // CONTROL AERATOR
    // =====================================================

    suspend fun controlAerator(
        aeratorId: Int,
        state: Boolean
    ): AeratorControlResponse =
        withContext(Dispatchers.IO) {

            val connection =
                createConnection(
                    ApiConstants.aeratorControl(
                        aeratorId
                    ),
                    "POST"
                )

            connection.doOutput = true

            val body =
                JSONObject().apply {

                    put(
                        "state",
                        state
                    )
                }

            connection.outputStream
                .bufferedWriter()
                .use {
                    it.write(
                        body.toString()
                    )
                }

            try {

                val responseCode =
                    connection.responseCode

                if (responseCode !in 200..299) {

                    val error =
                        connection.errorStream
                            ?.bufferedReader()
                            ?.use {
                                it.readText()
                            }

                    throw Exception(
                        "Relay control HTTP $responseCode: $error"
                    )
                }

                val response =
                    connection.inputStream
                        .bufferedReader()
                        .use {
                            it.readText()
                        }

                val json =
                    JSONObject(response)

                AeratorControlResponse(

                    success =
                        json.optBoolean(
                            "success",
                            false
                        ),

                    aerator_id =
                        json.optInt(
                            "aerator_id",
                            aeratorId
                        ),

                    aerator_name =
                        json.optString(
                            "aerator_name",
                            null
                        ),

                    relay_number =
                        json.optInt(
                            "relay_number",
                            0
                        ),

                    state =
                        json.optBoolean(
                            "state",
                            state
                        ),

                    device =
                        json.optString(
                            "device",
                            null
                        ),

                    esp32_ip =
                        json.optString(
                            "esp32_ip",
                            null
                        )
                )

            } finally {

                connection.disconnect()
            }
        }
}