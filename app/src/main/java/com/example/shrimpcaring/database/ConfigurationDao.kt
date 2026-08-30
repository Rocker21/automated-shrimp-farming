package com.example.shrimpcaring.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfigurationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(configuration: Configuration)

    @Update
    suspend fun update(configuration: Configuration)

    @Query("SELECT * FROM configuration WHERE pondId = :pondId LIMIT 1")
    suspend fun getConfigurationSync(pondId: Int): Configuration?

    @Query("SELECT * FROM configuration WHERE pondId = :pondId LIMIT 1")
    fun getConfiguration(
        pondId: Int
    ): Flow<Configuration?>

    @Query("DELETE FROM configuration WHERE pondId = :pondId")
    suspend fun deleteConfigurationForPond(pondId: Int)

}
