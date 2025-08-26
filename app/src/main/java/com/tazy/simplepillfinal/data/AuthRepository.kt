// F_ARQUIVO: data/AuthRepository.kt
package com.tazy.simplepillfinal.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tazy.simplepillfinal.model.Paciente
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
    suspend fun vincularPaciente(emailPaciente: String, associadoUid: String, associadoTipo: TipoUsuario) {
        // 1. Encontrar o paciente pelo e-mail na coleção de pacientes
        val querySnapshot = firestore.collection(FirestoreCollections.PACIENTES)
            .whereEqualTo("email", emailPaciente)
            .limit(1)
            .get()
            .await()

        if (querySnapshot.isEmpty) {
            throw Exception("Nenhum paciente encontrado com este e-mail.")
        }

        val pacienteDoc = querySnapshot.documents.first()
        val pacienteUid = pacienteDoc.id

        // 2. Determinar qual campo atualizar no documento do paciente
        val campoParaAtualizar = when (associadoTipo) {
            TipoUsuario.CUIDADOR -> "cuidadoresIds"
            TipoUsuario.PROFISSIONAL_SAUDE -> "profissionaisIds"
            else -> throw Exception("Tipo de perfil inválido para vinculação.")
        }

        // 3. Adicionar o UID do associado a um array no documento do paciente
        // Usamos FieldValue.arrayUnion para garantir que não haja UIDs duplicados
        val atualizacao = hashMapOf<String, Any>(
            campoParaAtualizar to com.google.firebase.firestore.FieldValue.arrayUnion(associadoUid)
        )

        firestore.collection(FirestoreCollections.PACIENTES).document(pacienteUid)
            .update(atualizacao)
            .await()
    }
    suspend fun getPacientesVinculados(uid: String, tipo: TipoUsuario): List<Paciente> {
        // 1. Determina o campo correto para a consulta com base no tipo de perfil
        val campoDeBusca = when (tipo) {
            TipoUsuario.CUIDADOR -> "cuidadoresIds"
            TipoUsuario.PROFISSIONAL_SAUDE -> "profissionaisIds"
            else -> throw IllegalArgumentException("Tipo de perfil inválido para buscar pacientes.")
        }

        // 2. Executa a consulta no Firestore
        val querySnapshot = firestore.collection(FirestoreCollections.PACIENTES)
            .whereArrayContains(campoDeBusca, uid) // 'whereArrayContains' é a chave aqui
            .get()
            .await()

        // 3. Mapeia os documentos encontrados para a nossa classe de dados Paciente
        return querySnapshot.documents.mapNotNull { doc ->
            doc.toObject(Paciente::class.java)
        }
    }
}