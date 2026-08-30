package com.example.shrimpcaring.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shrimpcaring.database.SavedWifi
import com.example.shrimpcaring.di.ServiceLocator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class WifiViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = ServiceLocator.provideSavedWifiDao(application)
    val savedWifiList: Flow<List<SavedWifi>> = dao.getAllSavedWifi()

    fun saveWifi(ssid: String, password: String) {
        viewModelScope.launch {
            dao.insertWifi(SavedWifi(ssid = ssid, password = password))
        }
    }

    fun deleteWifi(wifi: SavedWifi) {
        viewModelScope.launch {
            dao.deleteWifi(wifi)
        }
    }
}
