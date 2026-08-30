package com.example.shrimpcaring.repository

import com.example.shrimpcaring.database.SensorDao
import com.example.shrimpcaring.database.SensorEntity
import kotlinx.coroutines.flow.Flow

class SensorRepository(
    private val sensorDao: SensorDao
) {

    fun getAllLogs(): Flow<List<SensorEntity>> {
        return sensorDao.getAllLogs()
    }

    fun getLatestLog(): Flow<SensorEntity?> {
        return sensorDao.getLatestLog()
    }

    fun getLogsForPond(pondId: Int): Flow<List<SensorEntity>> {
        return sensorDao.getLogsForPond(pondId)
    }

    suspend fun insert(log: SensorEntity) {
        sensorDao.insert(log)
    }

    suspend fun insertAll(logs: List<SensorEntity>) {
        sensorDao.insertAll(logs)
    }

    suspend fun deleteAll() {
        sensorDao.deleteAll()
    }

    suspend fun deletePondLogs(pondId: Int) {
        sensorDao.deletePondLogs(pondId)
    }
}