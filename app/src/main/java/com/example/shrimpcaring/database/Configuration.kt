package com.example.shrimpcaring.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "configuration")
data class Configuration(

    @PrimaryKey(autoGenerate = true)
    val id:Int=0,

    val pondId:Int,

    val bluetoothName:String="",

    val wifiSSID:String="",

    val wifiPassword:String="",

    val samplingInterval:Int=5

)