package com.example.shrimpcaring.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shrimpcaring.di.ServiceLocator
import com.example.shrimpcaring.models.Pond
import com.example.shrimpcaring.repository.PondRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PondViewModel(
    application: Application
) : AndroidViewModel(application) {

    // =====================================================
    // REPOSITORY
    // =====================================================

    private val repository: PondRepository =
        ServiceLocator.providePondRepository(application)


    // =====================================================
    // LOCAL ROOM PONDS
    // =====================================================

    val allPonds: Flow<List<Pond>> =
        repository.ponds


    // =====================================================
    // SERVER PONDS
    // =====================================================

    private val _serverPonds =
        MutableStateFlow<List<Pond>>(emptyList())

    val serverPonds: StateFlow<List<Pond>> =
        _serverPonds.asStateFlow()


    // =====================================================
    // SERVER ERROR
    // =====================================================

    private val _serverError =
        MutableStateFlow<String?>(null)

    val serverError: StateFlow<String?> =
        _serverError.asStateFlow()


    // =====================================================
    // SERVER AERATOR COUNT
    //
    // null = loading / not checked
    // 0    = no aerators configured
    // > 0  = aerators already configured
    // =====================================================

    private val _serverAeratorCount =
        MutableStateFlow<Int?>(null)

    val serverAeratorCount: StateFlow<Int?> =
        _serverAeratorCount.asStateFlow()


    // =====================================================
    // CURRENT POND
    // =====================================================

    private val _currentPond =
        MutableStateFlow<Pond?>(null)

    val currentPond: StateFlow<Pond?> =
        _currentPond.asStateFlow()


    // =====================================================
    // LOAD PONDS FROM RASPBERRY PI
    // =====================================================

    fun loadServerPonds() {

        viewModelScope.launch {

            val result =
                repository.getServerPonds()

            result.onSuccess { apiPonds ->

                _serverPonds.value =
                    apiPonds.map { apiPond ->

                        Pond(
                            id = apiPond.id,
                            name = apiPond.name,
                            location = apiPond.location
                        )
                    }

                _serverError.value = null
            }

            result.onFailure { error ->

                _serverError.value =
                    error.message
                        ?: "Unable to load ponds"
            }
        }
    }


    // =====================================================
    // LOAD SERVER AERATOR COUNT
    // =====================================================

    fun loadServerAeratorCount(
        pondId: Int
    ) {

        // null means we're checking the server
        _serverAeratorCount.value = null

        viewModelScope.launch {

            val result =
                repository.getServerAeratorCount(
                    pondId
                )

            result.onSuccess { count ->

                _serverAeratorCount.value =
                    count

                _serverError.value =
                    null
            }

            result.onFailure { error ->

                _serverAeratorCount.value =
                    null

                _serverError.value =
                    error.message
                        ?: "Unable to load aerators"
            }
        }
    }


    // =====================================================
    // LOAD CURRENT POND
    // =====================================================

    fun loadPond(
        pondId: Int
    ) {

        viewModelScope.launch {

            /*
             * First try to find the pond that was already
             * downloaded from the Raspberry Pi.
             */

            val serverPond =
                _serverPonds.value
                    .find { pond ->
                        pond.id == pondId
                    }

            if (serverPond != null) {

                _currentPond.value =
                    serverPond

                return@launch
            }


            /*
             * Fallback to Room for compatibility with
             * existing parts of the application.
             */

            _currentPond.value =
                repository.getPondById(
                    pondId
                )
        }
    }


    // =====================================================
    // ADD POND TO RASPBERRY PI
    // =====================================================

    fun addPond(
        name: String,
        location: String
    ) {

        viewModelScope.launch {

            val result =
                repository.createServerPond(
                    name = name,
                    location = location
                )

            result.onSuccess {

                // Refresh server pond list
                loadServerPonds()

                _serverError.value =
                    null
            }

            result.onFailure { error ->

                _serverError.value =
                    error.message
                        ?: "Failed to create pond"
            }
        }
    }


    // =====================================================
    // DELETE LOCAL POND
    //
    // Kept temporarily because existing UI may still
    // reference this function.
    // =====================================================

    fun deletePond(
        pond: Pond
    ) {

        viewModelScope.launch {

            _serverError.value = null

            // Delete from server first
            val result =
                repository.deleteServerPond(
                    pond.id
                )

            result.onSuccess {

                // If server deletion succeeded, delete locally too
                repository.deletePond(
                    pond
                )

                // Refresh the list to reflect changes
                loadServerPonds()
            }

            result.onFailure { error ->

                _serverError.value =
                    error.message
                        ?: "Failed to delete pond from server"
                
                // Even if server fails, maybe the pond was already gone?
                // Or maybe we should still refresh to be sure.
                loadServerPonds()
            }
        }
    }


    // =====================================================
    // LEGACY LOCAL AERATOR COUNT
    //
    // Kept temporarily so AeratorCountScreen does not
    // break while we migrate configuration to the server.
    // =====================================================

    fun updateAeratorCount(
        pondId: Int,
        count: Int
    ) {

        viewModelScope.launch {

            val pond =
                repository.getPondById(
                    pondId
                )

            pond?.let {

                val updatedPond =
                    it.copy(
                        aeratorCount = count
                    )

                repository.updatePond(
                    updatedPond
                )

                _currentPond.value =
                    updatedPond
            }

            /*
             * Refresh the actual server count.
             *
             * Once AeratorCountScreen is fully server-based,
             * this legacy Room update can be removed.
             */
            loadServerAeratorCount(
                pondId
            )
        }
    }


    // =====================================================
    // CLEAR ERROR
    // =====================================================

    fun clearServerError() {

        _serverError.value =
            null
    }
}