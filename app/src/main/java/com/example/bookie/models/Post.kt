package com.example.bookie.models

import java.util.Date

class Post (
    var usuario: String,
    var titulo: String,
    var texto: String,
    var curtidas: Int,
    var comentarios: Int,
    var avaliacao: Float,
    var data_criacao: Date,
    var livro: Livro? = null,
)