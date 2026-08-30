package com.example.shrimpcaring.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DashboardViewModel : ViewModel() {

    private val _ph = MutableStateFlow(0.0)
    val ph: StateFlow<Double> = _ph

    private val _voltage = MutableStateFlow(0.0)
    val voltage: StateFlow<Double> = _voltage

    private val _current = MutableStateFlow(0.0)
    val current: StateFlow<Double> = _current

    private val _power = MutableStateFlow(0.0)
    val power: StateFlow<Double> = _power

    private val _energy = MutableStateFlow(0.0)
    val energy: StateFlow<Double> = _energy

    private val _frequency = MutableStateFlow(0.0)
    val frequency: StateFlow<Double> = _frequency

    private val _pf = MutableStateFlow(0.0)
    val pf: StateFlow<Double> = _pf

    fun update(
        ph: Double,
        voltage: Double,
        current: Double,
        power: Double,
        energy: Double,
        frequency: Double,
        pf: Double
    ) {
        _ph.value = ph
        _voltage.value = voltage
        _current.value = current
        _power.value = power
        _energy.value = energy
        _frequency.value = frequency
        _pf.value = pf
    }
}