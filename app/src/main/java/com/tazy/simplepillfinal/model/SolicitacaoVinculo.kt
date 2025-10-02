// NOVO ARQUIVO: model/SolicitacaoVinculo.kt
package com.tazy.simplepillfinal.model

import com.google.firebase.firestore.DocumentId

data class SolicitacaoVinculo(
    @DocumentId
    val id: String = "",
    val remetenteUid: String = "",
    val remetenteTipo: String = "",
    val destinatarioUid: String = ""
)