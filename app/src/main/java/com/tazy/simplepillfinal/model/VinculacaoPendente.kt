package com.tazy.simplepillfinal.model

import com.google.firebase.firestore.Exclude

// Modelo de dados que será salvo no Firestore
data class VinculacaoPendente(
    val solicitanteUid: String = "",
    val solicitanteNome: String = ""
)

// Modelo de dados para a UI, inclui o tipo de usuário
data class SolicitacaoVinculacao(
    val solicitanteUid: String,
    val solicitanteNome: String,
    val tipo: TipoUsuario
)