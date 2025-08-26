// CRIE ESTE NOVO ARQUIVO: model/Paciente.kt
package com.tazy.simplepillfinal.model

data class Paciente(
    val uid: String = "",
    val nome: String = "",
    val email: String = ""
    // Adicione outros campos que queira exibir na lista, como idade, etc.
)