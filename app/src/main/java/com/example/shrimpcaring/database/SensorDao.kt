package com.example.shrimpcaring.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SensorDao {

    @Query("SELECT * FROM sensor_logs ORDER BY timestamp DESC LIMIT 1")
    fun getLatestLog(): Flow<SensorEntity?>

    @Insert
    suspend fun insert(log: SensorEntity)

    @Insert
    suspend fun insertAll(logs: List<SensorEntity>)

    @Query("SELECT * FROM sensor_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<SensorEntity>>

    @Query("SELECT * FROM sensor_logs WHERE pondId = :pondId ORDER BY timestamp DESC")
    fun getLogsForPond(pondId: Int): Flow<List<SensorEntity>>

    @Query("DELETE FROM sensor_logs")
    suspend fun deleteAll()

    @Query("DELETE FROM sensor_logs WHERE pondId = :pondId")
    suspend fun deletePondLogs(pondId: Int)
}