package com.example.bookie.models

import kotlinx.serialization.Serializable

@Serializable
class Conversa (
    var id: String?,
    var mensagens: List<Mensagem>?,
    var usuario1: Usuario?,
    var usuario2: Usuario?,
): java.io.Serializable {
    constructor(): this(null, null, null, null)
}