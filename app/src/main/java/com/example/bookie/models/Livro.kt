package com.example.bookie.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
public class ImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null,
): java.io.Serializable


@Serializable
class VolumeInfo(
    @SerialName("imageLinks")
    var imageLinks: ImageLinks? = null,
    @SerialName("title")
    var nome: String? = null,
    @SerialName("authors")
    var autor: List<String>? = null,
    @SerialName("description")
    var sinopse: String? = null,
    @SerialName("pageCount")
    var paginas: Int? = null
): java.io.Serializable


@Serializable
data class Livro (
    var id: String? = null,
    var volumeInfo: VolumeInfo? = null,
    var favorito: Boolean? = null,
    var document: String? = null,
): java.io.Serializable {


    constructor(): this(null, null)


    public fun getAutor(): String {
        if (volumeInfo?.autor!!.isNotEmpty()) {
            return volumeInfo?.autor!![0];
        }


        return "";
    }


    public fun getCapa(): String {
        if (!volumeInfo?.imageLinks?.thumbnail.isNullOrBlank()) {
            return volumeInfo?.imageLinks?.thumbnail!!;
        }


        return ""
    }
}
