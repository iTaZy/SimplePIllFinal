// Usuario.kt
package com.tazy.simplepillfinal.model

/** Tipos possíveis de usuário */
enum class TipoUsuario {
    PACIENTE,
    CUIDADOR,
    PROFISSIONAL_SAUDE
}

/** Dados básicos do usuário */
data class Usuario(
    val nome: String,
    val tipo: TipoUsuario
)
