package com.example.shrimpcaring.repository

import com.example.shrimpcaring.database.PondDao
import com.example.shrimpcaring.database.ConfigurationDao
import com.example.shrimpcaring.database.SensorDao
import com.example.shrimpcaring.models.Pond
import com.example.shrimpcaring.network.ApiPond
import com.example.shrimpcaring.network.ShrimpApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PondRepository(

    private val dao: PondDao,
    private val configDao: ConfigurationDao? = null,
    private val sensorDao: SensorDao? = null

) {

    val ponds = dao.getAllPonds()
    private val api = ShrimpApi()
    suspend fun getServerAeratorCount(
        pondId: Int
    ): Result<Int> {

        return withContext(Dispatchers.IO) {
            try {
                val aerators = api.getPondAerators(pondId)
                Result.success(aerators.size)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun createServerPond(
        name: String,
        location: String
    ): Result<ApiPond> {

        return withContext(Dispatchers.IO) {
            try {
                Result.success(
                    api.createPond(name, location)
                )
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getServerPonds(): Result<List<ApiPond>> {

        return withContext(Dispatchers.IO) {

            try {

                Result.success(
                    api.getPonds()
                )

            } catch (e: Exception) {

                Result.failure(e)
            }
        }
    }
    suspend fun deleteServerPond(
        pondId: Int
    ): Result<Unit> {

        return withContext(Dispatchers.IO) {

            try {

                api.deletePond(pondId)

                Result.success(Unit)

            } catch (e: Exception) {

                Result.failure(e)
            }
        }
    }

    suspend fun addPond(

        pond: Pond

    ) {

        dao.insertPond(pond)

    }

    suspend fun deletePond(

        pond: Pond

    ) {

        dao.deletePond(pond)
        configDao?.deleteConfigurationForPond(pond.id)
        sensorDao?.deletePondLogs(pond.id)

    }

    suspend fun updatePond(

        pond: Pond

    ) {

        dao.updatePond(pond)

    }

    suspend fun getPondById(id: Int): Pond? = dao.getPondById(id)

}


