package com.example.shrimpcaring.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shrimpcaring.models.Pond
import com.example.shrimpcaring.repository.PondRepository
import kotlinx.coroutines.launch

class HomeViewModel(

    private val repository: PondRepository

) : ViewModel() {

    val ponds = repository.ponds

    fun addPond(

        name: String,

        location: String

    ) {

        viewModelScope.launch {

            repository.addPond(

                Pond(

                    name = name,

                    location = location

                )

            )

        }

    }

    fun deletePond(

        pond: Pond

    ) {

        viewModelScope.launch {

            repository.deletePond(pond)

        }

    }

}