package com.example.bookie.models

data class ClubeLeitura(
    val id: String = "",
    val nomeClube: String = "",
    val descricaoClube: String = "",
    val publico: Boolean = true,
    val Livro: Livro? = null,
    val membros: List<String> = emptyList()
) {
    constructor() : this("", "", "", true, null, emptyList())
}

