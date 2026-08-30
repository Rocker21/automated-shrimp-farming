package com.example.shrimpcaring.logger

import com.example.shrimpcaring.database.SensorEntity
import com.example.shrimpcaring.repository.SensorRepository
import com.example.shrimpcaring.viewmodel.MainViewModel
import kotlinx.coroutines.*

class LoggerManager(
    private val repository: SensorRepository,
    private val viewModel: MainViewModel
) {

    private var job: Job? = null

    fun startLogging(intervalMillis: Long) {
        if (job?.isActive == true) return

        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                try {
                    // Blynk is removed. In the future, sensor data could be fetched via Bluetooth or other API.
                    // For now, logging dummy data if no other source is available, or just skipping.
                    
                    val ph = 7.0 + (Math.random() * 0.4 - 0.2)
                    val voltage = 230.0 + (Math.random() * 5 - 2.5)
                    val current = 1.2 + (Math.random() * 0.2)
                    val watts = 270.0 + (Math.random() * 10)
                    val energy = 0.5
                    val frequency = 50.0
                    val pf = 0.98

                    val entity = SensorEntity(
                        pondId = 1,
                        timestamp = System.currentTimeMillis(),
                        ph = ph,
                        voltage = voltage,
                        current = current,
                        power = watts,
                        energy = energy,
                        frequency = frequency,
                        powerFactor = pf
                    )

                    repository.insert(entity)

                    viewModel.updateSensors(
                        ph,
                        voltage,
                        current,
                        watts,
                        energy,
                        frequency,
                        pf
                    )

                } catch (e: Exception) {
                    e.printStackTrace()
                }

                delay(intervalMillis)
            }
        }
    }

    fun stopLogging() {
        job?.cancel()
    }
}
