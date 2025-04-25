package com.tazy.simplepillfinal.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Medicacao(
    val nome: String,
    val horario: String,
    val comRefeicao: Boolean
)