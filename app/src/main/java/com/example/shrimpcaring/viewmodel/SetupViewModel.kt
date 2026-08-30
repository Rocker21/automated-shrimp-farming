package com.example.shrimpcaring.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SetupViewModel : ViewModel() {

    private val _ssid = MutableStateFlow("")
    val ssid: StateFlow<String> = _ssid.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _bleConnected = MutableStateFlow(false)
    val bleConnected: StateFlow<Boolean> = _bleConnected.asStateFlow()

    private val _wifiConnected = MutableStateFlow(false)
    val wifiConnected: StateFlow<Boolean> = _wifiConnected.asStateFlow()

    fun setSSID(value: String) {
        _ssid.value = value
    }

    fun setPassword(value: String) {
        _password.value = value
    }

    fun setBleConnected(state: Boolean) {
        _bleConnected.value = state
    }

    fun setWifiConnected(state: Boolean) {
        _wifiConnected.value = state
    }
}
