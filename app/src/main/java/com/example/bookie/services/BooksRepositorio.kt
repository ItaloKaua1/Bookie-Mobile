package com.example.bookie.services

import com.example.bookie.models.GoogleBooksResponse
import com.example.bookie.models.Livro
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class BooksRepositorio {
    suspend fun buscarLivros(texto: String): GoogleBooksResponse {
        val httpClient = HttpClient(Android) {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        return httpClient.get("https://www.googleapis.com/books/v1/volumes?q=${texto}").body()
    }
}