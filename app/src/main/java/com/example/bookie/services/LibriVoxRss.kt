package com.example.bookie.services

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LibriVoxRssInterface {
    @GET("rss")
    fun getRss (
        @Query("id") id: String
    ): Call<LibriVoxRss>
}

@Root(name = "rss", strict = false)
data class LibriVoxRss(
    @field:Element(name = "channel")
    var channel: Channel? = null
)

@Root(name = "channel", strict = false)
data class Channel(
    @field:Element(name = "title")
    var title: String? = null,

    @field:ElementList(inline = true)
    var items: List<Item>? = null
)

@Root(name = "item", strict = false)
data class Item(
    @field:Element(name = "title")
    var title: String? = null,

    @field:Element(name = "link")
    var link: String? = null,

    @field:Element(name = "enclosure", required = false)
    var enclosure: Enclosure? = null,

    @field:Element(name = "itunes:episode", required = false)
    var episode: String? = null,

    @field:Element(name = "itunes:duration", required = false)
    var duration: String? = null
)

@Root(name = "enclosure", strict = false)
data class Enclosure(
    @field:Element(name = "url", required = false)
    var url: String? = null
)