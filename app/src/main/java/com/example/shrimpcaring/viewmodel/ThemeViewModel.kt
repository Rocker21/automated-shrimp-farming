package com.example.shrimpcaring.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    
    companion object {
        private val _isDarkTheme = MutableStateFlow(false)
        val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()
    }

    private val sharedPreferences = application.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    
    init {
        _isDarkTheme.value = sharedPreferences.getBoolean("is_dark_theme", false)
    }

    fun toggleTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
        sharedPreferences.edit().putBoolean("is_dark_theme", isDark).apply()
    }
}
