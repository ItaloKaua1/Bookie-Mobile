package com.example.bookie.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Livro (
    @SerialName("subtitle")
    var capa: String = "",
    @SerialName("title")
    var nome: String,
    @SerialName("authors")
    var autor: Array<String> = arrayOf(),
    @SerialName("description")
    var sinopse: String = "",
)