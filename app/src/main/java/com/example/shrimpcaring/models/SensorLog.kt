package com.example.shrimpcaring.models

data class SensorLog(
    val date: String,
    val time: String,
    val ph: Double,
    val voltage: Double,
    val current: Double,
    val power: Double,
    val energy: Double,
    val frequency: Double,
    val powerFactor: Double
)