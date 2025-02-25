package com.example.bookie.models

import java.util.Date

data class Post(
    val id: String = "",
    val usuario: String = "",
    val titulo: String = "",
    val texto: String = "",
    val curtidas: Int = 0,
    val comentarios: Int = 0,
    val avaliacao: Float = 0f,
    val data_criacao: Date = Date(),
    val livro: Livro? = null,
    val photoUrl: String? = null,
)

data class Comment(
    val id: String = "",
    val usuario: String,
    val texto: String,
    val data: Date = Date()
)