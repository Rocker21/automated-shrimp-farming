package com.example.shrimpcaring.service

import android.app.*
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.shrimpcaring.R
import com.example.shrimpcaring.database.SensorEntity
import com.example.shrimpcaring.di.ServiceLocator
import com.example.shrimpcaring.repository.LoggerRepository
import kotlinx.coroutines.*

class LoggerService : Service() {

    companion object {
        const val CHANNEL_ID = "ShrimpLoggerChannel"
        const val NOTIFICATION_ID = 1001
    }

    private val serviceScope = CoroutineScope(
        Dispatchers.IO + SupervisorJob()
    )

    private var loggerJob: Job? = null
    private var currentPondId: Int = 1

    private lateinit var sensorRepository: com.example.shrimpcaring.repository.SensorRepository

    override fun onCreate() {
        super.onCreate()
        sensorRepository = ServiceLocator.provideRepository(this)

        createNotificationChannel()
        LoggerRepository.setRecording(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                buildNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            startForeground(
                NOTIFICATION_ID,
                buildNotification()
            )
        }
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        currentPondId = intent?.getIntExtra("pondId", 1) ?: 1
        val intervalString =
            intent?.getStringExtra("interval") ?: "10 sec"

        val interval = when (intervalString) {
            "5 sec" -> 5000L
            "10 sec" -> 10000L
            "30 sec" -> 30000L
            "1 min" -> 60000L
            "5 min" -> 300000L
            else -> 10000L
        }

        startLogger(interval)

        return START_STICKY
    }

    private fun startLogger(interval: Long) {
        if (loggerJob?.isActive == true)
            return

        loggerJob = serviceScope.launch {
            val bleManager = ServiceLocator.provideBleManager(this@LoggerService)
            
            while (isActive) {
                try {
                    val bleData = bleManager.lastSensorData
                    
                    val log = if (bleData != null && bleManager.connection.isConnected) {
                        SensorEntity(
                            pondId = currentPondId,
                            timestamp = System.currentTimeMillis(),
                            ph = bleData.ph.toDouble(),
                            voltage = bleData.voltage.toDouble(),
                            current = bleData.current.toDouble(),
                            power = bleData.power.toDouble(),
                            energy = bleData.energy.toDouble(),
                            frequency = bleData.frequency.toDouble(),
                            powerFactor = bleData.powerFactor.toDouble()
                        )
                    } else {
                        // Blynk is removed. Logging dummy data for now if no BLE.
                        SensorEntity(
                            pondId = currentPondId,
                            timestamp = System.currentTimeMillis(),
                            ph = 7.0 + (Math.random() * 0.4 - 0.2),
                            voltage = 230.0 + (Math.random() * 5 - 2.5),
                            current = 1.2 + (Math.random() * 0.2),
                            power = 270.0 + (Math.random() * 10),
                            energy = 0.5,
                            frequency = 50.0,
                            powerFactor = 0.98
                        )
                    }
                    
                    sensorRepository.insert(log)
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }

                delay(interval)
            }
        }
    }

    override fun onDestroy() {
        loggerJob?.cancel()
        serviceScope.cancel()
        LoggerRepository.setRecording(false)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(
            this,
            CHANNEL_ID
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Shrimp Caring")
            .setContentText("Recording sensor data...")
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Shrimp Logger",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager =
                getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
