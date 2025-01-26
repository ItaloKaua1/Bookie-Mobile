package com.example.bookie.models

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
class Mensagem (
    var corpo: String?,
    var usuario: Usuario?,
    @Serializable(DateSerializer::class)
    var dataHora: Date? = null,
): java.io.Serializable {
    constructor(): this(null, null, null)
}