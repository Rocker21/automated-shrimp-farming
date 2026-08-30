package com.example.shrimpcaring.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ponds")
data class Pond(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    val location: String,

    val deviceCount: Int = 0,

    val isOnline: Boolean = false,

    val aeratorCount: Int = 0

)