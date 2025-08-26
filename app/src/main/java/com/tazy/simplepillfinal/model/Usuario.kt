// Usuario.kt
package com.tazy.simplepillfinal.model

enum class TipoUsuario {
    PACIENTE,
    CUIDADOR,
    PROFISSIONAL_SAUDE
}

data class Usuario(
    val uid: String,
    val nome: String,
    val email: String,
    val tipo: TipoUsuario
)