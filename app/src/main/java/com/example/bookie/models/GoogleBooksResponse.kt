package com.example.bookie.models

import kotlinx.serialization.Serializable

@Serializable
class Item(
    val volumeInfo: Livro
)

@Serializable
class GoogleBooksResponse(
    val kind: String,
    val totalItems: Int,
    val items: Array<Item>
)