package com.example.bookie.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    @SerialName("id")
    val id: String,
    @SerialName("nome")
    val nome: String?,
    @SerialName("email")
    val email: String?,
    @SerialName("fotoPerfil")
    val fotoPerfil: String?,
    @SerialName("amigos")
    val amigos: List<String>? = emptyList()
) : java.io.Serializable {

    fun getPrimeiroNome(): String {
        return nome?.split(" ")?.firstOrNull() ?: ""
    }

    fun temAmigos(): Boolean {
        return amigos?.isNotEmpty() == true
    }
}