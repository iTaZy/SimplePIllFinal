package com.tazy.simplepillfinal.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.FieldValue
import com.tazy.simplepillfinal.model.*
import kotlinx.coroutines.tasks.await
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun criarUsuario(email: String, senha: String, perfis: Map<TipoUsuario, Map<String, Any>>): String {
        val authResult = auth.createUserWithEmailAndPassword(email, senha).await()
        val uid = authResult.user?.uid ?: throw Exception("Erro ao obter UID do Auth.")

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

    suspend fun enviarSolicitacaoVinculacao(emailPaciente: String, associadoUid: String, associadoTipo: TipoUsuario) {
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

        val campoParaAtualizar = when (associadoTipo) {
            TipoUsuario.CUIDADOR -> "cuidadoresPendentes"
            TipoUsuario.PROFISSIONAL_SAUDE -> "profissionaisPendentes"
            else -> throw Exception("Tipo de perfil inválido para solicitação de vinculação.")
        }

        val solicitanteDoc = firestore.collection(
            if (associadoTipo == TipoUsuario.CUIDADOR) FirestoreCollections.CUIDADORES
            else FirestoreCollections.PROFISSIONAIS_DA_SAUDE
        ).document(associadoUid).get().await()

        val solicitacao = VinculacaoPendente(
            solicitanteUid = associadoUid,
            solicitanteNome = solicitanteDoc.getString("nome") ?: "Nome Desconhecido"
        )

        val atualizacao = hashMapOf<String, Any>(
            campoParaAtualizar to FieldValue.arrayUnion(solicitacao)
        )

        firestore.collection(FirestoreCollections.PACIENTES).document(pacienteUid)
            .update(atualizacao)
            .await()
    }

    suspend fun getSolicitacoesPendentes(pacienteUid: String): List<SolicitacaoVinculacao> {
        val pacienteDoc = firestore.collection(FirestoreCollections.PACIENTES).document(pacienteUid).get().await()
        if (!pacienteDoc.exists()) {
            throw Exception("Paciente não encontrado.")
        }
        val profPendentes = pacienteDoc.get("profissionaisPendentes") as? List<Map<String, Any>>
        val cuidadorPendentes = pacienteDoc.get("cuidadoresPendentes") as? List<Map<String, Any>>

        val solicitacoes = mutableListOf<SolicitacaoVinculacao>()

        profPendentes?.forEach {
            solicitacoes.add(SolicitacaoVinculacao(
                solicitanteUid = it["solicitanteUid"] as String,
                solicitanteNome = it["solicitanteNome"] as String,
                tipo = TipoUsuario.PROFISSIONAL_SAUDE
            ))
        }
        cuidadorPendentes?.forEach {
            solicitacoes.add(SolicitacaoVinculacao(
                solicitanteUid = it["solicitanteUid"] as String,
                solicitanteNome = it["solicitanteNome"] as String,
                tipo = TipoUsuario.CUIDADOR
            ))
        }

        return solicitacoes
    }

    suspend fun aceitarVinculacao(pacienteUid: String, solicitanteUid: String, tipo: TipoUsuario) {
        val solicitacaoCampo = if (tipo == TipoUsuario.CUIDADOR) "cuidadoresPendentes" else "profissionaisPendentes"
        val vinculoCampo = if (tipo == TipoUsuario.CUIDADOR) "cuidadoresIds" else "profissionaisIds"

        val solicitanteDoc = firestore.collection(
            if (tipo == TipoUsuario.CUIDADOR) FirestoreCollections.CUIDADORES
            else FirestoreCollections.PROFISSIONAIS_DA_SAUDE
        ).document(solicitanteUid).get().await()

        val solicitacao = VinculacaoPendente(
            solicitanteUid = solicitanteUid,
            solicitanteNome = solicitanteDoc.getString("nome") ?: "Nome Desconhecido"
        )

        val pacienteRef = firestore.collection(FirestoreCollections.PACIENTES).document(pacienteUid)

        firestore.runTransaction { transaction ->
            val pacienteDoc = transaction.get(pacienteRef)
            val solicitacoesPendentes = (pacienteDoc.get(solicitacaoCampo) as? List<Map<String, Any>>)?.toMutableList() ?: mutableListOf()
            val vinculosExistentes = (pacienteDoc.get(vinculoCampo) as? List<String>)?.toMutableList() ?: mutableListOf()

            val solicitacaoParaRemover = solicitacoesPendentes.find { it["solicitanteUid"] == solicitanteUid }
            if (solicitacaoParaRemover != null) {
                solicitacoesPendentes.remove(solicitacaoParaRemover)
                if (solicitanteUid !in vinculosExistentes) {
                    vinculosExistentes.add(solicitanteUid)
                }
                transaction.update(pacienteRef, solicitacaoCampo, solicitacoesPendentes)
                transaction.update(pacienteRef, vinculoCampo, vinculosExistentes)
            }
        }.await()
    }

    suspend fun negarVinculacao(pacienteUid: String, solicitanteUid: String, tipo: TipoUsuario) {
        val solicitacaoCampo = if (tipo == TipoUsuario.CUIDADOR) "cuidadoresPendentes" else "profissionaisPendentes"

        val solicitanteDoc = firestore.collection(
            if (tipo == TipoUsuario.CUIDADOR) FirestoreCollections.CUIDADORES
            else FirestoreCollections.PROFISSIONAIS_DA_SAUDE
        ).document(solicitanteUid).get().await()

        val solicitacao = VinculacaoPendente(
            solicitanteUid = solicitanteUid,
            solicitanteNome = solicitanteDoc.getString("nome") ?: "Nome Desconhecido"
        )

        firestore.collection(FirestoreCollections.PACIENTES).document(pacienteUid)
            .update(solicitacaoCampo, FieldValue.arrayRemove(solicitacao))
            .await()
    }

    suspend fun getPacientesVinculados(uid: String, tipo: TipoUsuario): List<Paciente> {
        val campoDeBusca = when (tipo) {
            TipoUsuario.CUIDADOR -> "cuidadoresIds"
            TipoUsuario.PROFISSIONAL_SAUDE -> "profissionaisIds"
            else -> throw IllegalArgumentException("Tipo de perfil inválido para buscar pacientes.")
        }

        val querySnapshot = firestore.collection(FirestoreCollections.PACIENTES)
            .whereArrayContains(campoDeBusca, uid)
            .get()
            .await()

        return querySnapshot.documents.mapNotNull { doc ->
            doc.toObject(Paciente::class.java)
        }
    }

    suspend fun prescreverMedicacao(pacienteUid: String, nome: String, dosagem: String, frequencia: String, duracao: String, observacoes: String) {
        val medicacaoRef = firestore.collection(FirestoreCollections.MEDICACOES).document()

        val medicacao = Medicacao(
            id = medicacaoRef.id,
            pacienteUid = pacienteUid,
            nome = nome,
            dosagem = dosagem,
            frequencia = frequencia,
            duracao = duracao,
            observacoes = observacoes,
        )
        medicacaoRef.set(medicacao).await()
    }

    suspend fun getMedicacoes(pacienteUid: String): List<Medicacao> {
        val querySnapshot = firestore.collection(FirestoreCollections.MEDICACOES)
            .whereEqualTo("pacienteUid", pacienteUid)
            .orderBy("dataPrescricao", Query.Direction.DESCENDING)
            .get()
            .await()

        return querySnapshot.toObjects(Medicacao::class.java)
    }

    suspend fun salvarExame(pacienteUid: String, examePedido: String, unidade: String, diagnostico: String) {
        val exameRef = firestore.collection(FirestoreCollections.EXAMES).document()
        val exame = Exame(
            id = exameRef.id,
            pacienteUid = pacienteUid,
            examePedido = examePedido,
            unidade = unidade,
            diagnostico = diagnostico
        )
        exameRef.set(exame).await()
    }

    suspend fun salvarVacinacao(pacienteUid: String, vacina1: String, vacina2: String, vacina3: String, vacina4: String) {
        val vacinacaoRef = firestore.collection(FirestoreCollections.VACINACAO).document()
        val vacinacao = Vacinacao(
            id = vacinacaoRef.id,
            pacienteUid = pacienteUid,
            vacina1 = vacina1,
            vacina2 = vacina2,
            vacina3 = vacina3,
            vacina4 = vacina4
        )
        vacinacaoRef.set(vacinacao).await()
    }

    suspend fun salvarInternacao(pacienteUid: String, unidade: String, motivo: String, data: String) {
        val internacaoRef = firestore.collection(FirestoreCollections.INTERNACOES).document()
        val internacao = Internacao(
            id = internacaoRef.id,
            pacienteUid = pacienteUid,
            unidade = unidade,
            motivo = motivo,
            data = data
        )
        internacaoRef.set(internacao).await()
    }

    suspend fun salvarFisioterapia(pacienteUid: String, data: String, local: String, sessoes: String, diagnostico: String) {
        val fisioterapiaRef = firestore.collection(FirestoreCollections.FISIOTERAPIA).document()
        val fisioterapia = Fisioterapia(
            id = fisioterapiaRef.id,
            pacienteUid = pacienteUid,
            data = data,
            local = local,
            sessoes = sessoes,
            diagnostico = diagnostico
        )
        fisioterapiaRef.set(fisioterapia).await()
    }

    suspend fun salvarSaudeMental(pacienteUid: String, unidade: String, tratamento: String, data: String, duracao: String) {
        val saudeMentalRef = firestore.collection(FirestoreCollections.SAUDE_MENTAL).document()
        val saudeMental = SaudeMental(
            id = saudeMentalRef.id,
            pacienteUid = pacienteUid,
            unidade = unidade,
            tratamento = tratamento,
            data = data,
            duracao = duracao
        )
        saudeMentalRef.set(saudeMental).await()
    }

    suspend fun salvarNutricao(pacienteUid: String, data: String, local: String, diagnostico: String) {
        val nutricaoRef = firestore.collection(FirestoreCollections.NUTRICAO).document()
        val nutricao = Nutricao(
            id = nutricaoRef.id,
            pacienteUid = pacienteUid,
            data = data,
            local = local,
            diagnostico = diagnostico
        )
        nutricaoRef.set(nutricao).await()
    }

    suspend fun getExames(pacienteUid: String): List<Exame> {
        val querySnapshot = firestore.collection(FirestoreCollections.EXAMES)
            .whereEqualTo("pacienteUid", pacienteUid)
            .orderBy("dataSolicitacao", Query.Direction.DESCENDING)
            .get()
            .await()
        return querySnapshot.toObjects(Exame::class.java)
    }

    suspend fun getVacinacao(pacienteUid: String): List<Vacinacao> {
        val querySnapshot = firestore.collection(FirestoreCollections.VACINACAO)
            .whereEqualTo("pacienteUid", pacienteUid)
            .orderBy("dataSolicitacao", Query.Direction.DESCENDING)
            .get()
            .await()
        return querySnapshot.toObjects(Vacinacao::class.java)
    }

    suspend fun getInternacoes(pacienteUid: String): List<Internacao> {
        val querySnapshot = firestore.collection(FirestoreCollections.INTERNACOES)
            .whereEqualTo("pacienteUid", pacienteUid)
            .orderBy("dataRegistro", Query.Direction.DESCENDING)
            .get()
            .await()
        return querySnapshot.toObjects(Internacao::class.java)
    }

    suspend fun getFisioterapia(pacienteUid: String): List<Fisioterapia> {
        val querySnapshot = firestore.collection(FirestoreCollections.FISIOTERAPIA)
            .whereEqualTo("pacienteUid", pacienteUid)
            .orderBy("dataRegistro", Query.Direction.DESCENDING)
            .get()
            .await()
        return querySnapshot.toObjects(Fisioterapia::class.java)
    }

    suspend fun getSaudeMental(pacienteUid: String): List<SaudeMental> {
        val querySnapshot = firestore.collection(FirestoreCollections.SAUDE_MENTAL)
            .whereEqualTo("pacienteUid", pacienteUid)
            .orderBy("dataRegistro", Query.Direction.DESCENDING)
            .get()
            .await()
        return querySnapshot.toObjects(SaudeMental::class.java)
    }

    suspend fun getNutricao(pacienteUid: String): List<Nutricao> {
        val querySnapshot = firestore.collection(FirestoreCollections.NUTRICAO)
            .whereEqualTo("pacienteUid", pacienteUid)
            .orderBy("dataRegistro", Query.Direction.DESCENDING)
            .get()
            .await()
        return querySnapshot.toObjects(Nutricao::class.java)
    }

    // CORREÇÃO: Busca as informações de um profissional de saúde por ID
    suspend fun getUsuario(uid: String): Usuario? {
        return try {
            val userDoc = firestore.collection(FirestoreCollections.PROFISSIONAIS_DA_SAUDE).document(uid).get().await()
            // Assume que o documento na coleção de profissionais também é um objeto Usuario
            userDoc.toObject(Usuario::class.java)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Falha ao buscar usuário: ${e.message}")
            null
        }
    }

    suspend fun getPaciente(pacienteUid: String): Paciente? {
        return try {
            val pacienteDoc = firestore.collection(FirestoreCollections.PACIENTES).document(pacienteUid).get().await()
            pacienteDoc.toObject(Paciente::class.java)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Falha ao buscar paciente: ${e.message}")
            null
        }
    }

    // CORREÇÃO: Remove a vinculação apenas do documento do paciente
    suspend fun desfazerVinculo(pacienteUid: String, profissionalUid: String, password: String) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            throw Exception("Nenhum usuário logado.")
        }

        val credential = EmailAuthProvider.getCredential(currentUser.email!!, password)
        currentUser.reauthenticate(credential).await()

        firestore.runTransaction { transaction ->
            val pacienteRef = firestore.collection(FirestoreCollections.PACIENTES).document(pacienteUid)
            val pacienteDoc = transaction.get(pacienteRef)

            val profissionaisIds = pacienteDoc.toObject(Paciente::class.java)?.profissionaisIds.orEmpty().toMutableList()
            profissionaisIds.remove(profissionalUid)
            transaction.update(pacienteRef, "profissionaisIds", profissionaisIds)

        }.await()
    }
}