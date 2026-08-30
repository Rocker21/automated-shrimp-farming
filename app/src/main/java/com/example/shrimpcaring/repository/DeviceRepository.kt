package com.example.shrimpcaring.repository

import com.example.shrimpcaring.database.DeviceDao
import com.example.shrimpcaring.models.Device

class DeviceRepository(
    private val dao: DeviceDao
) {

    fun getDevices(
        pondId:Int
    ) = dao.getDevices(pondId)

    suspend fun addDevice(
        device: Device
    ){
        dao.insert(device)
    }

    suspend fun deleteDevice(
        device: Device
    ){
        dao.delete(device)
    }

}