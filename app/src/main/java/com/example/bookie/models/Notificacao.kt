package com.example.bookie.models

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
class Notificacao(
    var id: String? = null,
    var titulo: String? = null,
    var corpo: String? = null,
    var usuarioDestino: List<String>? = null,
    var usuarioOrigem: String? = null,
    @Serializable(DateSerializer::class)
    var dataHora: Date? = null,
): java.io.Serializable
