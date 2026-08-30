package com.example.shrimpcaring.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shrimpcaring.models.Pond
import com.example.shrimpcaring.models.Device

@Database(
    entities = [
        Pond::class,
        Device::class,
        Configuration::class,
        SensorEntity::class,
        SavedWifi::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pondDao(): PondDao
    abstract fun deviceDao(): DeviceDao
    abstract fun configurationDao(): ConfigurationDao
    abstract fun sensorDao(): SensorDao
    abstract fun savedWifiDao(): SavedWifiDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shrimp_caring_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
