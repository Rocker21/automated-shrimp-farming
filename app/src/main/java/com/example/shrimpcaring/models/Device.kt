package com.example.shrimpcaring.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class Device(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val pondId: Int,

    val type: String,

    val name: String,

    val configured: Boolean = false,

    val enabled: Boolean = true

)