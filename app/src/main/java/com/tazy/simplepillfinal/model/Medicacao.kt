// F_ARQUIVO: itazy/simplepillfinal/SimplePIllFinal-23165cb55ae68d55ff279be231b459964f532606/app/src/main/java/com/tazy/simplepillfinal/model/Medicacao.kt
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
    val dataPrescricao: Timestamp = Timestamp.now(),
    val arquivoUrl: String? = null // Adicionar este campo para a URL do PDF
)