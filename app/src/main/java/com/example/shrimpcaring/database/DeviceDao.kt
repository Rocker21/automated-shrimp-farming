package com.example.shrimpcaring.database

import androidx.room.*
import com.example.shrimpcaring.models.Device
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {

    @Query("SELECT * FROM devices WHERE pondId=:pondId")
    fun getDevices(pondId:Int): Flow<List<Device>>

    @Insert
    suspend fun insert(device: Device)

    @Update
    suspend fun update(device: Device)

    @Delete
    suspend fun delete(device: Device)

}