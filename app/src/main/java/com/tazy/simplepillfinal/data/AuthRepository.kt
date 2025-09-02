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

    // ATUALIZAÇÃO: A função agora recebe um mapa de perfis para criar
    suspend fun criarUsuario(email: String, senha: String, perfis: Map<TipoUsuario, Map<String, Any>>): String {
        val authResult = auth.createUserWithEmailAndPassword(email, senha).await()
        val uid = authResult.user?.uid ?: throw Exception("Erro ao obter UID do Auth.")

        // Itera sobre o mapa de perfis e cria um documento para cada um
        for ((tipo, data) in perfis) {
            val collectionPath = when (tipo) {
                TipoUsuario.PACIENTE -> FirestoreCollections.PACIENTES
                TipoUsuario.CUIDADOR -> FirestoreCollections.CUIDADORES
                TipoUsuario.PROFISSIONAL_SAUDE -> FirestoreCollections.PROFISSIONAIS_DA_SAUDE
            }
            val dataComUid = data.toMutableMap().apply {
                put("uid", uid)
                put("email", email)
            }
            firestore.collection(collectionPath).document(uid).set(dataComUid).await()
        }

        return uid
    }

    suspend fun autenticar(email: String, senha: String, tipo: TipoUsuario): Usuario {
        val authResult = auth.signInWithEmailAndPassword(email, senha).await()
        val uid = authResult.user?.uid ?: throw Exception("Usuário não encontrado.")

        val collectionPath = when (tipo) {
            TipoUsuario.PACIENTE -> FirestoreCollections.PACIENTES
            TipoUsuario.CUIDADOR -> FirestoreCollections.CUIDADORES
            TipoUsuario.PROFISSIONAL_SAUDE -> FirestoreCollections.PROFISSIONAIS_DA_SAUDE
        }

        val userDoc = firestore.collection(collectionPath).document(uid).get().await()
        if (userDoc.exists()) {
            return Usuario(uid, userDoc.getString("nome")!!, email, tipo)
        }

        throw Exception("Dados não encontrados para este tipo de perfil. Verifique o perfil selecionado.")
    }
}