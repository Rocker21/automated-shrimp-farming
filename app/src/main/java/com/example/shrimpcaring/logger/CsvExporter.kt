package com.example.shrimpcaring.logger

import android.content.Context
import android.net.Uri
import com.example.shrimpcaring.database.SensorEntity
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CsvExporter {

    fun exportToCsv(
        context: Context,
        uri: Uri,
        logs: List<SensorEntity>
    ): Boolean {

        return try {

            val outputStream =
                context.contentResolver.openOutputStream(uri)

            val writer = BufferedWriter(
                OutputStreamWriter(outputStream)
            )

            writer.write(
                "Date,Time,pH,Voltage(V),Current(A),Power(W),Energy(kWh),Frequency(Hz),PowerFactor"
            )

            writer.newLine()

            val sdfDate =
                SimpleDateFormat(
                    "dd-MM-yyyy",
                    Locale.getDefault()
                )

            val sdfTime =
                SimpleDateFormat(
                    "HH:mm:ss",
                    Locale.getDefault()
                )

            logs.forEach { log ->

                val date = Date(log.timestamp)

                writer.write(
                    "${sdfDate.format(date)}," +
                            "${sdfTime.format(date)}," +
                            "${log.ph}," +
                            "${log.voltage}," +
                            "${log.current}," +
                            "${log.power}," +
                            "${log.energy}," +
                            "${log.frequency}," +
                            log.powerFactor
                )

                writer.newLine()

            }

            writer.flush()
            writer.close()

            true

        } catch (e: Exception) {

            e.printStackTrace()
            false

        }

    }

}