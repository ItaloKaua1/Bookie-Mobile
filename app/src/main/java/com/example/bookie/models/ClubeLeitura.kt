package com.example.bookie.models

data class ClubeLeitura(
    val id: String,
    val nomeClube: String,
    val descricaoClube: String,
    val publico: Boolean,
    val Livro: Livro,
    val membros: MutableList<String>
)
