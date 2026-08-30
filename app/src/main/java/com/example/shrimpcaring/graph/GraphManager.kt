package com.example.shrimpcaring.graph

import com.example.shrimpcaring.database.SensorEntity
import com.github.mikephil.charting.data.Entry

object GraphManager {

    fun getPhEntries(
        logs: List<SensorEntity>
    ): List<Entry> {

        return logs.mapIndexed { index, log ->
            Entry(index.toFloat(), log.ph.toFloat())
        }

    }

    fun getVoltageEntries(
        logs: List<SensorEntity>
    ): List<Entry> {

        return logs.mapIndexed { index, log ->
            Entry(index.toFloat(), log.voltage.toFloat())
        }

    }

    fun getCurrentEntries(
        logs: List<SensorEntity>
    ): List<Entry> {

        return logs.mapIndexed { index, log ->
            Entry(index.toFloat(), log.current.toFloat())
        }

    }

    fun getPowerEntries(
        logs: List<SensorEntity>
    ): List<Entry> {

        return logs.mapIndexed { index, log ->
            Entry(index.toFloat(), log.power.toFloat())
        }

    }

    fun getEnergyEntries(
        logs: List<SensorEntity>
    ): List<Entry> {

        return logs.mapIndexed { index, log ->
            Entry(index.toFloat(), log.energy.toFloat())
        }

    }

    fun getFrequencyEntries(
        logs: List<SensorEntity>
    ): List<Entry> {

        return logs.mapIndexed { index, log ->
            Entry(index.toFloat(), log.frequency.toFloat())
        }

    }

    fun getPowerFactorEntries(
        logs: List<SensorEntity>
    ): List<Entry> {

        return logs.mapIndexed { index, log ->
            Entry(index.toFloat(), log.powerFactor.toFloat())
        }

    }

}