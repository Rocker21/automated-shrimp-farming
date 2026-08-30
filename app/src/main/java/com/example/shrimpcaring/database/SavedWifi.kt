package com.example.shrimpcaring.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_wifi")
data class SavedWifi(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ssid: String,
    val password: String
)
