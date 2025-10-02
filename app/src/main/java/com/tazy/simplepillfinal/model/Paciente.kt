// CRIE ESTE NOVO ARQUIVO: model/Paciente.kt
package com.tazy.simplepillfinal.model

data class Paciente(
    val uid: String = "",
    val nome: String = "",
    val email: String = "",
    val profissionaisIds: List<String> = emptyList(), // lista de IDs de profissionais vinculados
    val cuidadoresIds: List<String> = emptyList(), // lista de IDs de cuidadores vinculados
    val profissionaisPendentes: List<VinculacaoPendente> = emptyList(), // lista de profissionais com solicitacoes pendentes
    val cuidadoresPendentes: List<VinculacaoPendente> = emptyList() // lista de cuidadores com solicitacoes pendentes
)