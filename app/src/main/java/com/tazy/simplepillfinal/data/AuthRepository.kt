// F_ARQUIVO: data/AuthRepository.kt
package com.tazy.simplepillfinal.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tazy.simplepillfinal.model.TipoUsuario
import com.tazy.simplepillfinal.model.Usuario
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun criarUsuario(email: String, senha: String, tipo: TipoUsuario, data: Map<String, Any>): Usuario {
        val authResult = auth.createUserWithEmailAndPassword(email, senha).await()
        val uid = authResult.user?.uid ?: throw Exception("Erro ao obter UID do Auth.")

        val dataComUid = data.toMutableMap().apply {
            put("uid", uid)
            put("email", email)
        }

        val collectionPath = when (tipo) {
            TipoUsuario.PACIENTE -> FirestoreCollections.PACIENTES
            TipoUsuario.CUIDADOR -> FirestoreCollections.CUIDADORES
            TipoUsuario.PROFISSIONAL_SAUDE -> FirestoreCollections.PROFISSIONAIS_DA_SAUDE
        }

        firestore.collection(collectionPath).document(uid).set(dataComUid).await()

        return Usuario(
            uid = uid,
            nome = data["nome"] as String,
            email = email,
            tipo = tipo
        )
    }

    suspend fun autenticar(email: String, senha: String): Usuario {
        val authResult = auth.signInWithEmailAndPassword(email, senha).await()
        val uid = authResult.user?.uid ?: throw Exception("Usuário não encontrado.")

        // Verifica em qual coleção o UID existe
        var userDoc = firestore.collection(FirestoreCollections.PACIENTES).document(uid).get().await()
        if (userDoc.exists()) {
            return Usuario(uid, userDoc.getString("nome")!!, email, TipoUsuario.PACIENTE)
        }

        userDoc = firestore.collection(FirestoreCollections.CUIDADORES).document(uid).get().await()
        if (userDoc.exists()) {
            return Usuario(uid, userDoc.getString("nome")!!, email, TipoUsuario.CUIDADOR)
        }

        userDoc = firestore.collection(FirestoreCollections.PROFISSIONAIS_DA_SAUDE).document(uid).get().await()
        if (userDoc.exists()) {
            return Usuario(uid, userDoc.getString("nome")!!, email, TipoUsuario.PROFISSIONAL_SAUDE)
        }

        throw Exception("Dados do usuário não encontrados no banco de dados.")
    }
}