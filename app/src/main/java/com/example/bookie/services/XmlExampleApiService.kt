package com.example.bookie.services


import com.example.bookie.models.AudioBookModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path


interface XmlExampleApiService {
    @Headers("Accept: application/xml")
    @GET("rss/{id}")
    suspend fun getAudioBook(@Path("id") id: Int): Response<AudioBookModel>
}
