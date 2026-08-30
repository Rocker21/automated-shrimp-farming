package com.example.shrimpcaring.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class AeratorRepository(

    private val api: ShrimpApi = ShrimpApi()

) {

    // ======================================================
    // CHECK RASPBERRY PI SERVER
    // ======================================================

    suspend fun checkServer(): Result<ServerStatus> {

        return withContext(Dispatchers.IO) {

            try {

                val status =
                    api.getServerStatus()

                Result.success(status)

            } catch (e: Exception) {

                Result.failure(e)
            }
        }
    }


    // ======================================================
    // GET ALL PONDS
    // ======================================================

    suspend fun getPonds(): Result<List<ApiPond>> {

        return withContext(Dispatchers.IO) {

            try {

                val ponds =
                    api.getPonds()

                Result.success(ponds)

            } catch (e: Exception) {

                Result.failure(e)
            }
        }
    }


    // ======================================================
    // GET AERATORS FOR POND
    // ======================================================

    suspend fun getPondAerators(
        pondId: Int
    ): Result<List<Aerator>> {

        return withContext(Dispatchers.IO) {

            try {

                val aerators =
                    api.getPondAerators(
                        pondId
                    )

                Result.success(aerators)

            } catch (e: Exception) {

                Result.failure(e)
            }
        }
    }


    // ======================================================
    // GET AERATORS FOR ESP32
    // ======================================================

    suspend fun getDeviceAerators(
        deviceId: Int
    ): Result<List<Aerator>> {

        return withContext(Dispatchers.IO) {

            try {

                val aerators =
                    api.getDeviceAerators(
                        deviceId
                    )

                Result.success(aerators)

            } catch (e: Exception) {

                Result.failure(e)
            }
        }
    }


    // ======================================================
    // TURN AERATOR ON
    // ======================================================

    suspend fun turnOn(
        aeratorId: Int
    ): Result<AeratorControlResponse> {

        return controlAerator(
            aeratorId = aeratorId,
            state = true
        )
    }


    // ======================================================
    // TURN AERATOR OFF
    // ======================================================

    suspend fun turnOff(
        aeratorId: Int
    ): Result<AeratorControlResponse> {

        return controlAerator(
            aeratorId = aeratorId,
            state = false
        )
    }


    // ======================================================
    // CONTROL AERATOR
    // ======================================================

    suspend fun controlAerator(
        aeratorId: Int,
        state: Boolean
    ): Result<AeratorControlResponse> {

        return withContext(Dispatchers.IO) {

            try {

                val response =
                    api.controlAerator(
                        aeratorId = aeratorId,
                        state = state
                    )

                if (response.success) {

                    Result.success(response)

                } else {

                    Result.failure(
                        Exception(
                            "Aerator command failed"
                        )
                    )
                }

            } catch (e: Exception) {

                Result.failure(e)
            }
        }
    }
}