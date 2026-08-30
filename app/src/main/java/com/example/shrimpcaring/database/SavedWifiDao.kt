package com.example.shrimpcaring.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedWifiDao {
    @Query("SELECT * FROM saved_wifi ORDER BY ssid ASC")
    fun getAllSavedWifi(): Flow<List<SavedWifi>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWifi(wifi: SavedWifi)

    @Delete
    suspend fun deleteWifi(wifi: SavedWifi)
}
