package com.example.bookie.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LibriVoxApi {
    @GET("audiobooks")
    fun getAudiobook (
        @Query("id") id: String,
        @Query("format") format: String = "json"
        ): Call<LibriVoxResponse>
}

data class LibriVoxResponse(
    val books: List<Book>
)

data class Book(
    val id: String,
    val title: String,
    val url_librivox: String,
    val url_rss: String,
    val url_text_source: String,
    val authors: List<Author>,
    val sections: List<Section>
)

data class Author(
    val id: String,
    val first_name: String,
    val last_name: String
)

data class Section(
    val id: String,
    val title: String,
    val url: String,
    val url_librivox: String
)