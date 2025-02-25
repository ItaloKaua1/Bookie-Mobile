package com.example.bookie.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class ImageLinks(
    val smallThumbnail: String? = null,
    var thumbnail: String? = null,
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
    var disponivelTroca: Boolean? = null,
    var usuario: Usuario? = null,
    var lido: Boolean? = false,
    var lendo: Boolean? = false,
    var queroLer: Boolean? = false,
): java.io.Serializable {

    constructor(): this(null, null, null, null, null, null, null, null, null)

    fun getAutor(): String {
        if (volumeInfo == null) {
            return ""
        }

        val autores = volumeInfo!!.autor

        if (autores == null || autores.isEmpty()) {
            return ""
        }

        return autores[0]
    }

    fun getCapa(): String {
        if (!volumeInfo?.imageLinks?.thumbnail.isNullOrBlank()) {
            return volumeInfo?.imageLinks?.thumbnail!!
        }

        return ""
    }

    fun atualizarStatus(novoStatus: String) {
        lido = novoStatus == "lido"
        lendo = novoStatus == "lendo"
        queroLer = novoStatus == "quero ler"
    }
}
