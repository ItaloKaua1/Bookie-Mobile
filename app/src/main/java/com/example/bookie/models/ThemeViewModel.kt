package com.example.bookie.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ThemeViewModel : ViewModel() {
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> get() = _isDarkTheme

    fun toggleTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
    }
}
