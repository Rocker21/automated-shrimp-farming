package com.example.shrimpcaring.database

import androidx.room.*
import com.example.shrimpcaring.models.Pond
import kotlinx.coroutines.flow.Flow

@Dao
interface PondDao {

    @Query("SELECT * FROM ponds ORDER BY id ASC")
    fun getAllPonds(): Flow<List<Pond>>

    @Query("SELECT * FROM ponds WHERE id = :pondId")
    suspend fun getPondById(pondId: Int): Pond?

    @Insert
    suspend fun insertPond(pond: Pond)

    @Update
    suspend fun updatePond(pond: Pond)

    @Delete
    suspend fun deletePond(pond: Pond)

}