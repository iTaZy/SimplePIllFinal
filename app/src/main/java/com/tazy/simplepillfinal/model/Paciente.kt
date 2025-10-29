// ARQUIVO CORRIGIDO: model/Paciente.kt
package com.tazy.simplepillfinal.model

data class Paciente(
    val uid: String = "",
    val nome: String = "",
    val email: String = "",
    val telefone: String = "",
    val idade: String = "",
    val endereco: String = "",
    val profissao: String = "",
    val nacionalidade: String = "",
    val numSus: String = "",
    val unidadeSus: String = "",
    val cpf: String = "", // ADICIONADO CAMPO CPF
    val profissionaisIds: List<String> = emptyList(),
    val cuidadoresIds: List<String> = emptyList(),
    val profissionaisPendentes: List<VinculacaoPendente> = emptyList(),
    val cuidadoresPendentes: List<VinculacaoPendente> = emptyList()
)