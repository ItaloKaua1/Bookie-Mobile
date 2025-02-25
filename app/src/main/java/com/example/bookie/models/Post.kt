package com.example.bookie.models

import java.util.Date

data class Post(
    val usuario: String = "",
    val titulo: String = "",
    val texto: String = "",
    val curtidas: Int = 0,
    val comentarios: Int = 0,
    val avaliacao: Float = 0f,
    val data_criacao: Date = Date(),
    val livro: Livro? = null
)