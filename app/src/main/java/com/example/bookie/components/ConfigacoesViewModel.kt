package com.example.bookie.components

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ConfiguracoesViewModel : ViewModel() {
    private val _temaEscuro = MutableStateFlow(false)
    val temaEscuro: StateFlow<Boolean> = _temaEscuro

    fun alternarTema() {
        _temaEscuro.value = !_temaEscuro.value
    }
}