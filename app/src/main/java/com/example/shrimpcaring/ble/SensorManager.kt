package com.example.shrimpcaring.ble

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class SensorData(
    val voltage: Float = 0f,
    val current: Float = 0f,
    val power: Float = 0f,
    val energy: Float = 0f,
    val frequency: Float = 0f,
    val powerFactor: Float = 0f,
    val ph: Float = 0f,
    val dissolvedOxygen: Float = 0f,
    val temperature: Float = 0f,
    val rawString: String = ""
)

class SensorManager(
    private val writer: BleWriter
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    
    // Use replay=1 to ensure MainViewModel gets the latest data as soon as it subscribes
    private val _sensorDataFlow = MutableSharedFlow<SensorData>(replay = 1)
    val sensorDataFlow = _sensorDataFlow.asSharedFlow()

    var onSensorDataReceived: ((SensorData) -> Unit)? = null

    fun requestData(): Boolean {
        return writer.requestSensorData()
    }

    fun parseSensorData(rawData: String) {
        val cleanData = rawData.trim().filter { it.isPrintable() }
        if (cleanData.isEmpty()) return
        
        println("BLE RAW DATA RECEIVED: [$cleanData]")
        
        try {
            // Split by comma, semicolon or colon
            val values = cleanData.split(Regex("[,;:]")).map { it.trim() }
            
            if (values.isEmpty()) return

            val sensorData = SensorData(
                voltage = values.getOrNull(0)?.toFloatOrNull() ?: 0f,
                current = values.getOrNull(1)?.toFloatOrNull() ?: 0f,
                power = values.getOrNull(2)?.toFloatOrNull() ?: 0f,
                energy = values.getOrNull(3)?.toFloatOrNull() ?: 0f,
                frequency = values.getOrNull(4)?.toFloatOrNull() ?: 0f,
                powerFactor = values.getOrNull(5)?.toFloatOrNull() ?: 0f,
                ph = values.getOrNull(6)?.toFloatOrNull() ?: 0f,
                dissolvedOxygen = values.getOrNull(7)?.toFloatOrNull() ?: 0f,
                temperature = values.getOrNull(8)?.toFloatOrNull() ?: 0f,
                rawString = cleanData
            )

            println("BLE PARSED: $sensorData")

            // Notify legacy listener
            onSensorDataReceived?.invoke(sensorData)
            
            // Emit to Flow
            scope.launch {
                _sensorDataFlow.emit(sensorData)
            }

        } catch (e: Exception) {
            println("BLE PARSE ERROR: ${e.message}")
        }
    }
    
    private fun Char.isPrintable(): Boolean {
        val block = Character.UnicodeBlock.of(this)
        return (!Character.isISOControl(this)) &&
                this != Character.MIN_VALUE &&
                block != null &&
                block != Character.UnicodeBlock.SPECIALS
    }
}
