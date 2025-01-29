package com.example.bookie.models


import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Text
import org.simpleframework.xml.Root


@Root(name = "rss", strict=false)
data class AudioBookModel @JvmOverloads constructor (
    @field:Element(name = "channel")
    var channel: Channel? = null
)


@Root(name = "channel", strict = false)
data class Channel @JvmOverloads constructor (
    @field:ElementList(inline = true)
    var itemList: List<Item>? = null
)


@Root(name = "item", strict = false)
data class Item @JvmOverloads constructor (
    @field:Element(name = "enclosure", required = false)
//    @Attribute(name = "enclosure")
    var enclosure: Enclosure? = null
)


@Root(name = "enclosure", strict = false)
data class Enclosure @JvmOverloads constructor (
    @field:Attribute(name = "url")
    var url: String? = null
)
