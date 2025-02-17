package com.example.bookie.models

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
class TrocaOferecida(
    var observacao: String? = null,
    var livroOferecido: Livro? = null,
    var usuario: Usuario? = null,
    var trocaDisponivel: TrocaDisponivel? = null,
    var status: String? = null,
    @Serializable(DateSerializer::class)
    var dataHora: Date? = null,
    var document: String? = null,
): java.io.Serializable {
    constructor(): this(null, null, null, null, null, null, null)
}