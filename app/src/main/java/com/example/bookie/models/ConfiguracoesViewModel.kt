package com.example.bookie.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookie.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConfiguracoesViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(application)

    private val _themeOption = MutableStateFlow(ThemeOption.LIGHT)
    val themeOption: StateFlow<ThemeOption> = _themeOption

    private val _notificacoesAtivadas = MutableStateFlow(false)
    val notificacoesAtivadas: StateFlow<Boolean> = _notificacoesAtivadas

    private val _animacoesAtivadas = MutableStateFlow(false)
    val animacoesAtivadas: StateFlow<Boolean> = _animacoesAtivadas

    init {
        viewModelScope.launch {
            userRepository.customTheme.collect { theme ->
                _themeOption.value = when(theme) {
                    "dark" -> ThemeOption.DARK
                    "auto" -> ThemeOption.AUTO
                    else -> ThemeOption.LIGHT
                }
            }
        }
        viewModelScope.launch {
            userRepository.notificationsEnabled.collect { notificationsEnabled ->
                _notificacoesAtivadas.value = notificationsEnabled
            }
        }
        viewModelScope.launch {
            userRepository.animationsEnabled.collect { animationsEnabled ->
                _animacoesAtivadas.value = animationsEnabled
            }
        }
    }

    fun setThemeOption(option: ThemeOption) {
        viewModelScope.launch {
            _themeOption.value = option
            val themeString = when(option) {
                ThemeOption.DARK -> "dark"
                ThemeOption.AUTO -> "auto"
                else -> "light"
            }
            userRepository.updateCustomTheme(themeString)
        }
    }

    fun alternarNotificacoes(ativado: Boolean) {
        viewModelScope.launch {
            userRepository.updateNotificationsEnabled(ativado)
        }
    }

    fun alternarAnimacoes(habilitado: Boolean) {
        viewModelScope.launch {
            userRepository.updateAnimationsEnabled(habilitado)
        }
    }
}
