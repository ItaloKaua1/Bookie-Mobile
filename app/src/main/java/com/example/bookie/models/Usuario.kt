package com.example.bookie.models

import kotlinx.serialization.Serializable

@Serializable
class Usuario (
    var id: String?,
    var email: String?,
    var nome: String?,
): java.io.Serializable {
    constructor(): this(null, null, null)
}
