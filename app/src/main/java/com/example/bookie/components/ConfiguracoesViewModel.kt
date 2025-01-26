package com.example.bookie.components

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookie.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ConfiguracoesViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(application)

    private val _temaEscuro = MutableStateFlow(false)
    val temaEscuro: StateFlow<Boolean> = _temaEscuro

    private val _notificacoesAtivadas = MutableStateFlow(false)
    val notificacoesAtivadas: StateFlow<Boolean> = _notificacoesAtivadas

    private val _animacoesAtivadas = MutableStateFlow(false)
    val animacoesAtivadas: StateFlow<Boolean> = _animacoesAtivadas

    init {
        viewModelScope.launch {
            userRepository.customTheme.collect { theme ->
                _temaEscuro.value = theme == "dark"
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

    fun alternarTema() {
        viewModelScope.launch {
            val novoTema = if (_temaEscuro.value) "light" else "dark"
            userRepository.updateCustomTheme(novoTema)
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