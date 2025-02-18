package com.example.bookie.services

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApiService {
    @GET("volumes")
    suspend fun buscarLivros(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 10
    ): Response<LivroResponse>
}

data class LivroResponse(
    val items: List<LivroItem>
)

data class LivroItem(
    val id: String,
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val imageLinks: ImageLinks?
)

data class ImageLinks(
    val thumbnail: String
)