package com.example.bookie.services

import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

object RetrofitRss {
    private const val BASE_URL = "https://librivox.org/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .build()

    val api: LibriVoxRssInterface = retrofit.create(LibriVoxRssInterface::class.java)
}