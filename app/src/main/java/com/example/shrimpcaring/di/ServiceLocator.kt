package com.example.shrimpcaring.di

import android.content.Context
import com.example.shrimpcaring.ble.BleManager
import com.example.shrimpcaring.database.AppDatabase
import com.example.shrimpcaring.repository.PondRepository
import com.example.shrimpcaring.repository.SensorRepository

object ServiceLocator {

    @Volatile
    private var database: AppDatabase? = null

    @Volatile
    private var repository: SensorRepository? = null

    @Volatile
    private var pondRepository: PondRepository? = null

    @Volatile
    private var bleManager: BleManager? = null

    fun provideDatabase(
        context: Context
    ): AppDatabase {

        return database ?: synchronized(this) {

            val instance = AppDatabase.getDatabase(
                context.applicationContext
            )

            database = instance

            instance

        }

    }

    fun provideRepository(
        context: Context
    ): SensorRepository {

        return repository ?: synchronized(this) {

            val repo = SensorRepository(
                provideDatabase(context).sensorDao()
            )

            repository = repo

            repo

        }

    }

    fun providePondRepository(
        context: Context
    ): PondRepository {
        return pondRepository ?: synchronized(this) {
            val db = provideDatabase(context)
            val repo = PondRepository(
                dao = db.pondDao(),
                configDao = db.configurationDao(),
                sensorDao = db.sensorDao()
            )
            pondRepository = repo
            repo
        }
    }

    fun provideBleManager(
        context: Context
    ): BleManager {
        return bleManager ?: synchronized(this) {
            val manager = BleManager(context.applicationContext)
            bleManager = manager
            manager
        }
    }

    fun provideSavedWifiDao(context: Context) = provideDatabase(context).savedWifiDao()

}
