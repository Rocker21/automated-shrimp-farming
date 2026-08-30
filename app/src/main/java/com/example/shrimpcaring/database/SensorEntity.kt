package com.example.shrimpcaring.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sensor_logs")
data class SensorEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val pondId: Int,

    val timestamp: Long,

    val ph: Double,

    val voltage: Double,

    val current: Double,

    val power: Double,

    val energy: Double,

    val frequency: Double,

    val powerFactor: Double
)