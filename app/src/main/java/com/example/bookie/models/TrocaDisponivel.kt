package com.example.bookie.models

import kotlinx.serialization.Serializable

@Serializable
class TrocaDisponivel(
    var estado: String? = null,
    var observacao: String? = null,
    var livro: Livro? = null,
    var usuario: Usuario? = null,
    var finalizada: Boolean? = null,
    var localizacao: String? = null,
    var document: String? = null,
): java.io.Serializable {
    constructor(): this(null, null, null, null, null, null, null)
}