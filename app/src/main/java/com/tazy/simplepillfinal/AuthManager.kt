package com.tazy.simplepillfinal.auth

import kotlinx.coroutines.delay
import com.tazy.simplepillfinal.model.Usuario
import com.tazy.simplepillfinal.model.TipoUsuario

/**
 * Simula uma chamada de login assíncrona.
 * Substituap esse método pela sua integração real (Firebase, REST API, etc).
 */
suspend fun autenticar(email: String, senha: String): Usuario {
    // Simula tempo de rede
    delay(1000)

    // Lógica de exemplo: determina tipo pelo e-mail
    val tipo = when {
        email.contains("pro")  -> TipoUsuario.PROFISSIONAL_SAUDE
        email.contains("cui")  -> TipoUsuario.CUIDADOR
        else                   -> TipoUsuario.PACIENTE
    }
    // Nome fictício a partir do e-mail
    val nome = email.substringBefore("@").replace(".", " ").replaceFirstChar { it.uppercase() }
    return Usuario(nome = nome, tipo = tipo)
}
