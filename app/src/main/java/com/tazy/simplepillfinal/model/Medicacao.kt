// F_ARQUIVO: itazy/simplepillfinal/SimplePIllFinal-23165cb55ae68d037f9980688663ae0c4fa23193/app/src/main/java/com/tazy/simplepillfinal/model/Medicacao.kt
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
    val profissionalUid: String = "", // Adicionado para identificar o profissional
    val profissionalNome: String = "",
    val dataPrescricao: Timestamp = Timestamp.now(),
    val arquivoUrl: String? = null
)