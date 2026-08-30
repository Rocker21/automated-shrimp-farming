package com.example.shrimpcaring.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.shrimpcaring.database.SensorEntity
import com.example.shrimpcaring.di.ServiceLocator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class LoggerWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {

        private const val WORK_NAME = "SHRIMP_LOGGER"

        fun start(
            context: Context,
            intervalSeconds: Long
        ) {

            val request =
                OneTimeWorkRequestBuilder<LoggerWorker>()
                    .setInitialDelay(
                        intervalSeconds,
                        TimeUnit.SECONDS
                    )
                    .setInputData(
                        workDataOf(
                            "interval" to intervalSeconds
                        )
                    )
                    .build()

            WorkManager
                .getInstance(context)
                .enqueueUniqueWork(
                    WORK_NAME,
                    androidx.work.ExistingWorkPolicy.REPLACE,
                    request
                )

        }

        fun stop(
            context: Context
        ) {

            WorkManager
                .getInstance(context)
                .cancelUniqueWork(WORK_NAME)

        }

    }

    override suspend fun doWork(): Result {

        return withContext(Dispatchers.IO) {

            try {

                val repository =
                    ServiceLocator.provideRepository(applicationContext)

                // ------------------------------------------------
                // Use dummy values for now
                // ------------------------------------------------

                val ph = 7.10
                val voltage = 230.5
                val current = 0.45
                val power = 103.8
                val energy = 2.34
                val frequency = 50.0
                val pf = 0.98

                repository.insert(

                    SensorEntity(

                        pondId = 1,

                        timestamp = System.currentTimeMillis(),

                        ph = ph,

                        voltage = voltage,

                        current = current,

                        power = power,

                        energy = energy,

                        frequency = frequency,

                        powerFactor = pf

                    )

                )

                // Schedule next run

                val interval =
                    inputData.getLong(
                        "interval",
                        10L
                    )

                start(
                    applicationContext,
                    interval
                )

                Result.success()

            } catch (e: Exception) {

                e.printStackTrace()

                Result.retry()

            }

        }

    }

}