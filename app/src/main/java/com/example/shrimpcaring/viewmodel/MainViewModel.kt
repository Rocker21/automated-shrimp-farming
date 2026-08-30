package com.example.shrimpcaring.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shrimpcaring.di.ServiceLocator
import com.example.shrimpcaring.network.ShrimpApi
import com.example.shrimpcaring.repository.LoggerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sensorRepository =
        ServiceLocator.provideRepository(
            application
        )

    private val shrimpApi =
        ShrimpApi()


    // =====================================================
    // ACTIVE POND
    // =====================================================

    private val _activePondId =
        MutableStateFlow<Int?>(null)

    val activePondId:
            StateFlow<Int?> =
        _activePondId.asStateFlow()


    // =====================================================
    // CONNECTION
    // =====================================================

    private val _bleConnected =
        MutableStateFlow(false)

    val bleConnected:
            StateFlow<Boolean> =
        _bleConnected.asStateFlow()


    private val _wifiConnected =
        MutableStateFlow(false)

    val wifiConnected:
            StateFlow<Boolean> =
        _wifiConnected.asStateFlow()


    private val _pzemConnected =
        MutableStateFlow(false)

    val pzemConnected:
            StateFlow<Boolean> =
        _pzemConnected.asStateFlow()


    // =====================================================
    // SENSOR VALUES
    // =====================================================

    private val _ph =
        MutableStateFlow(0.0)

    val ph:
            StateFlow<Double> =
        _ph.asStateFlow()


    private val _voltage =
        MutableStateFlow(0.0)

    val voltage:
            StateFlow<Double> =
        _voltage.asStateFlow()


    private val _current =
        MutableStateFlow(0.0)

    val current:
            StateFlow<Double> =
        _current.asStateFlow()


    private val _power =
        MutableStateFlow(0.0)

    val power:
            StateFlow<Double> =
        _power.asStateFlow()


    private val _energy =
        MutableStateFlow(0.0)

    val energy:
            StateFlow<Double> =
        _energy.asStateFlow()


    private val _frequency =
        MutableStateFlow(0.0)

    val frequency:
            StateFlow<Double> =
        _frequency.asStateFlow()


    private val _powerFactor =
        MutableStateFlow(0.0)

    val powerFactor:
            StateFlow<Double> =
        _powerFactor.asStateFlow()


    // =====================================================
    // RAW DATA
    // =====================================================

    private val _rawBleData =
        MutableStateFlow("")

    val rawBleData:
            StateFlow<String> =
        _rawBleData.asStateFlow()


    // =====================================================
    // RELAYS
    // =====================================================

    private val _relay1 =
        MutableStateFlow(false)

    val relay1:
            StateFlow<Boolean> =
        _relay1.asStateFlow()


    private val _relay2 =
        MutableStateFlow(false)

    val relay2:
            StateFlow<Boolean> =
        _relay2.asStateFlow()


    private val _relay3 =
        MutableStateFlow(false)

    val relay3:
            StateFlow<Boolean> =
        _relay3.asStateFlow()


    private val _relay4 =
        MutableStateFlow(false)

    val relay4:
            StateFlow<Boolean> =
        _relay4.asStateFlow()


    // =====================================================
    // DATABASE / LOGGING
    // =====================================================

    val recording:
            StateFlow<Boolean> =
        LoggerRepository.isRecording


    val logs:
            Flow<List<com.example.shrimpcaring.database.SensorEntity>> =
        sensorRepository.getAllLogs()


    val recordCount:
            StateFlow<Int> =
        sensorRepository
            .getAllLogs()
            .map {
                it.size
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                0
            )


    // =====================================================
    // INIT
    // =====================================================

    init {

        // -----------------------------------------------
        // Load latest saved sensor values
        // -----------------------------------------------

        viewModelScope.launch {

            sensorRepository
                .getLatestLog()
                .collect { log ->

                    log?.let {

                        _ph.value =
                            it.ph

                        _voltage.value =
                            it.voltage

                        _current.value =
                            it.current

                        _power.value =
                            it.power

                        _energy.value =
                            it.energy

                        _frequency.value =
                            it.frequency

                        _powerFactor.value =
                            it.powerFactor
                    }
                }
        }


        // -----------------------------------------------
        // Continuous PZEM polling
        // -----------------------------------------------

        viewModelScope.launch(
            Dispatchers.IO
        ) {

            while (true) {

                val pondId =
                    _activePondId.value

                if (
                    pondId != null
                ) {

                    fetchPzemData(
                        pondId
                    )
                }

                delay(2000)
            }
        }
    }


    // =====================================================
    // SET ACTIVE POND
    // =====================================================

    fun setActivePond(
        pondId: Int
    ) {

        println(
            "PZEM: ACTIVE POND = $pondId"
        )

        _activePondId.value =
            pondId

        viewModelScope.launch(
            Dispatchers.IO
        ) {

            fetchPzemData(
                pondId
            )
        }
    }


    // =====================================================
    // FETCH PZEM DATA
    // =====================================================

    private suspend fun fetchPzemData(
        pondId: Int
    ) {

        println(
            "PZEM: REQUESTING POND $pondId"
        )

        try {

            val data =
                shrimpApi.getPondSensors(
                    pondId
                )

            println(
                "PZEM: RESPONSE RECEIVED"
            )

            println(
                "PZEM: success=${data.success}"
            )

            println(
                "PZEM: device=${data.device}"
            )

            println(
                "PZEM: connected=${data.pzemConnected}"
            )

            println(
                "PZEM: V=${data.voltage}"
            )

            println(
                "PZEM: A=${data.current}"
            )

            println(
                "PZEM: W=${data.power}"
            )

            println(
                "PZEM: kWh=${data.energy}"
            )

            println(
                "PZEM: Hz=${data.frequency}"
            )

            println(
                "PZEM: PF=${data.powerFactor}"
            )


            // ---------------------------------------------
            // Update UI
            // ---------------------------------------------

            _pzemConnected.value =
                data.pzemConnected

            _voltage.value =
                data.voltage

            _current.value =
                data.current

            _power.value =
                data.power

            _energy.value =
                data.energy

            _frequency.value =
                data.frequency

            _powerFactor.value =
                data.powerFactor

            _wifiConnected.value =
                true

        } catch (e: Exception) {

            println(
                "PZEM ERROR: ${e.message}"
            )

            e.printStackTrace()

            _pzemConnected.value =
                false

            _wifiConnected.value =
                false
        }
    }


    // =====================================================
    // MANUAL REFRESH
    // =====================================================

    fun requestSensorData() {

        val pondId =
            _activePondId.value

        if (
            pondId == null
        ) {

            println(
                "PZEM: No active pond"
            )

            return
        }

        viewModelScope.launch(
            Dispatchers.IO
        ) {

            fetchPzemData(
                pondId
            )
        }
    }


    // =====================================================
    // BLE STATUS
    // =====================================================

    fun setBleConnected(
        value: Boolean
    ) {

        _bleConnected.value =
            value
    }


    fun setWifiConnected(
        value: Boolean
    ) {

        _wifiConnected.value =
            value
    }


    // =====================================================
    // SENSOR UPDATE
    //
    // Kept for compatibility with other parts of app.
    // PZEM normally comes from Raspberry Pi now.
    // =====================================================

    fun updateSensors(
        ph: Double,
        voltage: Double,
        current: Double,
        power: Double,
        energy: Double,
        frequency: Double,
        powerFactor: Double
    ) {

        _ph.value =
            ph

        _voltage.value =
            voltage

        _current.value =
            current

        _power.value =
            power

        _energy.value =
            energy

        _frequency.value =
            frequency

        _powerFactor.value =
            powerFactor
    }


    // =====================================================
    // RELAY STATE
    //
    // Actual relay control is handled by AeratorRepository.
    // These functions only update local state.
    // =====================================================

    fun setRelay1(
        state: Boolean
    ) {
        _relay1.value =
            state
    }


    fun setRelay2(
        state: Boolean
    ) {
        _relay2.value =
            state
    }


    fun setRelay3(
        state: Boolean
    ) {
        _relay3.value =
            state
    }


    fun setRelay4(
        state: Boolean
    ) {
        _relay4.value =
            state
    }


    // =====================================================
    // CLEAR LOGS
    // =====================================================

    fun clearLogs() {

        viewModelScope.launch {

            sensorRepository.deleteAll()
        }
    }


    // =====================================================
    // CSV
    // =====================================================

    suspend fun getLogsCsvContent():
            String {

        val allLogs =
            sensorRepository
                .getAllLogs()
                .first()

        if (
            allLogs.isEmpty()
        ) {
            return ""
        }


        val header =
            "ID,Timestamp,Date,pH,Voltage,Current,Power,Energy,Frequency,PowerFactor\n"


        val dateFormat =
            SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss",
                Locale.getDefault()
            )


        val content =
            StringBuilder(
                header
            )


        allLogs.forEach { log ->

            content.append(
                "${log.id},"
            )

            content.append(
                "${log.timestamp},"
            )

            content.append(
                "${dateFormat.format(
                    Date(log.timestamp)
                )},"
            )

            content.append(
                "${log.ph},"
            )

            content.append(
                "${log.voltage},"
            )

            content.append(
                "${log.current},"
            )

            content.append(
                "${log.power},"
            )

            content.append(
                "${log.energy},"
            )

            content.append(
                "${log.frequency},"
            )

            content.append(
                "${log.powerFactor}\n"
            )
        }


        return content.toString()
    }
}