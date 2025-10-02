// F_ARQUIVO: data/AuthRepository.kt
package com.tazy.simplepillfinal.data

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.tazy.simplepillfinal.model.*
import kotlinx.coroutines.tasks.await

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

    suspend fun vincularPaciente(emailPaciente: String, associadoUid: String, associadoTipo: TipoUsuario) {
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

        // Ao invés de vincular diretamente, cria uma solicitação
        val solicitacao = SolicitacaoVinculo(
            remetenteUid = associadoUid,
            remetenteTipo = associadoTipo.name,
            destinatarioUid = pacienteUid
        )
        firestore.collection("solicitacoesVinculo").add(solicitacao).await()
    }

    // NOVA FUNÇÃO: Obtém solicitações de vínculo pendentes para um usuário
    suspend fun getSolicitacoesPendentes(uid: String): List<SolicitacaoVinculo> {
        val querySnapshot = firestore.collection("solicitacoesVinculo")
            .whereEqualTo("destinatarioUid", uid)
            .get()
            .await()
        return querySnapshot.toObjects(SolicitacaoVinculo::class.java)
    }

    // NOVA FUNÇÃO: Aceita uma solicitação de vínculo
    suspend fun aceitarVinculacao(solicitacao: SolicitacaoVinculo) {
        val pacienteDocRef = firestore.collection(FirestoreCollections.PACIENTES).document(solicitacao.destinatarioUid)

        val campoParaAtualizar = when (solicitacao.remetenteTipo) {
            "CUIDADOR" -> "cuidadoresIds"
            "PROFISSIONAL_SAUDE" -> "profissionaisIds"
            else -> throw Exception("Tipo de perfil inválido para vinculação.")
        }

        pacienteDocRef.update(campoParaAtualizar, FieldValue.arrayUnion(solicitacao.remetenteUid)).await()

        // Remove a solicitação após aceitar
        firestore.collection("solicitacoesVinculo").document(solicitacao.id).delete().await()
    }

    // NOVA FUNÇÃO: Nega uma solicitação de vínculo
    suspend fun negarVinculacao(solicitacaoId: String) {
        firestore.collection("solicitacoesVinculo").document(solicitacaoId).delete().await()
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

    suspend fun getProfissionaisECuidadoresVinculados(pacienteUid: String): List<Usuario> {
        val pacienteDoc = firestore.collection(FirestoreCollections.PACIENTES).document(pacienteUid).get().await()
        if (!pacienteDoc.exists()) {
            return emptyList()
        }

        val profissionaisIds = pacienteDoc.get("profissionaisIds") as? List<String> ?: emptyList()
        val cuidadoresIds = pacienteDoc.get("cuidadoresIds") as? List<String> ?: emptyList()

        val vinculados = mutableListOf<Usuario>()

        if (profissionaisIds.isNotEmpty()) {
            val profQuery = firestore.collection(FirestoreCollections.PROFISSIONAIS_DA_SAUDE)
                .whereIn("uid", profissionaisIds)
                .get()
                .await()
            vinculados.addAll(profQuery.documents.map { doc ->
                Usuario(doc.id, doc.getString("nome") ?: "", doc.getString("email") ?: "", TipoUsuario.PROFISSIONAL_SAUDE)
            })
        }

        if (cuidadoresIds.isNotEmpty()) {
            val cuidadorQuery = firestore.collection(FirestoreCollections.CUIDADORES)
                .whereIn("uid", cuidadoresIds)
                .get()
                .await()
            vinculados.addAll(cuidadorQuery.documents.map { doc ->
                Usuario(doc.id, doc.getString("nome") ?: "", doc.getString("email") ?: "", TipoUsuario.CUIDADOR)
            })
        }

        return vinculados
    }

    // NOVA FUNÇÃO ADICIONADA AQUI
    suspend fun getMedicosVinculados(uid: String, tipo: TipoUsuario): List<Medico> {
        // A lógica assume que um Paciente (uid) quer ver os seus médicos vinculados.
        if (tipo != TipoUsuario.PACIENTE) {
            // Se um cuidador ou médico tentar usar esta função, retorna uma lista vazia ou lança um erro.
            // Ajuste conforme a necessidade da sua aplicação.
            return emptyList()
        }

        val pacienteDoc = firestore.collection(FirestoreCollections.PACIENTES).document(uid).get().await()
        if (!pacienteDoc.exists()) {
            return emptyList()
        }

        // Pega a lista de IDs dos profissionais de saúde do documento do paciente
        val profissionaisIds = pacienteDoc.get("profissionaisIds") as? List<String> ?: emptyList()

        if (profissionaisIds.isEmpty()) {
            return emptyList()
        }

        // Busca na coleção de PROFISSIONAIS_DA_SAUDE todos os documentos cujos UIDs estão na lista
        val medicosQuery = firestore.collection(FirestoreCollections.PROFISSIONAIS_DA_SAUDE)
            .whereIn("uid", profissionaisIds)
            .get()
            .await()

        // Mapeia os documentos encontrados para objetos da classe Medico
        return medicosQuery.documents.mapNotNull { doc ->
            doc.toObject(Medico::class.java)
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
}
