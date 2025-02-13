package com.example.bookie.models

data class ThematicList(
    val id: String = "",
    val nome: String = "",
    val descricao: String = "",
    val livros: List<Livro> = emptyList()
) {
    constructor() : this("", "", "", emptyList())
}
