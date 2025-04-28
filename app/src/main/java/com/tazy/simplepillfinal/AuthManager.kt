package com.tazy.simplepillfinal.auth

import com.google.firebase.firestore.FirebaseFirestore
import com.tazy.simplepillfinal.model.TipoUsuario
import com.tazy.simplepillfinal.model.Usuario
import kotlinx.coroutines.tasks.await

/** Faz login real no Firebase Firestore buscando em várias coleções */
suspend fun autenticar(email: String, senha: String): Usuario {
    val db = FirebaseFirestore.getInstance()

    // 1. Buscar em profissionais da saúde
    val profissionais = db.collection("profissionaisDaSaude")
        .whereEqualTo("emailPaciente", email)
        .whereEqualTo("senhaPaciente", senha)
        .get()
        .await()

    if (!profissionais.isEmpty) {
        val documento = profissionais.documents.first()
        val nome = documento.getString("nome") ?: "Profissional da Saúde"
        return Usuario(nome = nome, tipo = TipoUsuario.PROFISSIONAL_SAUDE)
    }

    // 2. Buscar em pacientes
    val pacientes = db.collection("pacientes")
        .whereEqualTo("email", email)
        .whereEqualTo("senha", senha)
        .get()
        .await()

    if (!pacientes.isEmpty) {
        val documento = pacientes.documents.first()
        val nome = documento.getString("nome") ?: "Paciente"
        return Usuario(nome = nome, tipo = TipoUsuario.PACIENTE)
    }

    // 3. Buscar em cuidadores
    val cuidadores = db.collection("cuidadores")
        .whereEqualTo("email", email)
        .whereEqualTo("senha", senha)
        .get()
        .await()

    if (!cuidadores.isEmpty) {
        val documento = cuidadores.documents.first()
        val nome = documento.getString("nome") ?: "Cuidador"
        return Usuario(nome = nome, tipo = TipoUsuario.CUIDADOR)
    }

    // Se não encontrar em nenhum lugar
    throw Exception("Email ou senha incorretos")
}
