package com.example.bookie.models

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    var id: String? = null,
    var email: String? = null,
    var nome: String? = null,
    var bio: String? = null,
    var photoUrl: String? = null
) : java.io.Serializable {
    constructor(): this(null, null, null, null, null)
}
