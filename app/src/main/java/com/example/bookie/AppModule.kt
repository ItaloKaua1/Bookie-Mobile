package com.example.bookie

import com.example.bookie.services.XmlExampleApiService
import com.google.android.datatransport.runtime.dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Singleton


@Singleton
@Provides
fun provideXmlExampleApiServices(): XmlExampleApiService {
    return Retrofit.Builder()
        .baseUrl("https://librivox.org/")
        .client(OkHttpClient())
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .build()
        .create(XmlExampleApiService::class.java)
}
