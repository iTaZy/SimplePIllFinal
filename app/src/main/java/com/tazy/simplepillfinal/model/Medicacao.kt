// CRIE ESTE NOVO ARQUIVO: model/Medicacao.kt
package com.tazy.simplepillfinal.model

import com.google.firebase.Timestamp

data class Medicacao(
    val id: String = "",
    val nome: String = "",
    val dosagem: String = "",
    val frequencia: String = "",
    val duracao: String = "",
    val observacoes: String = "",
    val pacienteUid: String = "",
    val profissionalNome: String = "",
    val dataPrescricao: Timestamp = Timestamp.now()
)