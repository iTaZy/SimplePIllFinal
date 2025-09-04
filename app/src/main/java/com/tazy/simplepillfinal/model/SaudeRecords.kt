// F_ARQUIVO: model/SaudeRecords.kt
package com.tazy.simplepillfinal.model

import com.google.firebase.Timestamp

data class Exame(
    val id: String = "",
    val pacienteUid: String = "",
    val examePedido: String = "",
    val unidade: String = "",
    val diagnostico: String = "",
    val dataSolicitacao: Timestamp = Timestamp.now()
)

data class Vacinacao(
    val id: String = "",
    val pacienteUid: String = "",
    val vacina1: String = "",
    val vacina2: String = "",
    val vacina3: String = "",
    val vacina4: String = "",
    val dataSolicitacao: Timestamp = Timestamp.now()
)

data class Internacao(
    val id: String = "",
    val pacienteUid: String = "",
    val unidade: String = "",
    val motivo: String = "",
    val data: String = "",
    val dataRegistro: Timestamp = Timestamp.now()
)

data class Fisioterapia(
    val id: String = "",
    val pacienteUid: String = "",
    val data: String = "",
    val local: String = "",
    val sessoes: String = "",
    val diagnostico: String = "",
    val dataRegistro: Timestamp = Timestamp.now()
)

data class SaudeMental(
    val id: String = "",
    val pacienteUid: String = "",
    val unidade: String = "",
    val tratamento: String = "",
    val data: String = "",
    val duracao: String = "",
    val dataRegistro: Timestamp = Timestamp.now()
)

data class Nutricao(
    val id: String = "",
    val pacienteUid: String = "",
    val data: String = "",
    val local: String = "",
    val diagnostico: String = "",
    val dataRegistro: Timestamp = Timestamp.now()
)