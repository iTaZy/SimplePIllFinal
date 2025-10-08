// F_ARQUIVO: itazy/simplepillfinal/SimplePIllFinal-23165cb55ae68d55ff279be231b459964f532606/app/src/main/java/com/tazy/simplepillfinal/data/AuthRepository.kt
package com.tazy.simplepillfinal.data

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.tazy.simplepillfinal.model.*
import kotlinx.coroutines.tasks.await
import com.google.firebase.storage.FirebaseStorage
import android.net.Uri

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    private suspend fun uploadFile(docId: String, folder: String, uri: Uri?): String? {
        return if (uri != null) {
            val storageRef = storage.reference.child("${folder}/${docId}.pdf")
            storageRef.putFile(uri).await()
            storageRef.downloadUrl.await().toString()
        } else {
            null
        }
    }

    suspend fun criarUsuario(email: String, senha: String, perfis: Map<TipoUsuario, Map<String, Any>>): String {
        try {
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
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw Exception("O e-mail já está em uso ou a senha é inválida.")
        } catch (e: Exception) {
            throw Exception(e.message ?: "Ocorreu um erro desconhecido durante o cadastro.")
        }
    }

    suspend fun autenticar(email: String, senha: String, tipo: TipoUsuario): Usuario {
        try {
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
        } catch (e: FirebaseAuthInvalidUserException) {
            throw Exception("E-mail não cadastrado ou desativado.")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw Exception("Senha incorreta. Tente novamente.")
        } catch (e: Exception) {
            throw Exception(e.message ?: "Ocorreu um erro desconhecido durante o login.")
        }
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun reauthenticateUser(password: String) {
        val user = auth.currentUser ?: throw Exception("Usuário não logado.")
        val credential = EmailAuthProvider.getCredential(user.email!!, password)
        try {
            user.reauthenticate(credential).await()
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw Exception("Senha incorreta. Por favor, insira sua senha novamente.")
        } catch (e: Exception) {
            throw Exception(e.message ?: "Falha na reautenticação. Tente novamente.")
        }
    }

    suspend fun desvincularMedico(pacienteUid: String, medicoUid: String) {
        val pacienteDocRef = firestore.collection(FirestoreCollections.PACIENTES).document(pacienteUid)

        pacienteDocRef.update("profissionaisIds", FieldValue.arrayRemove(medicoUid)).await()
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

        val solicitacao = SolicitacaoVinculo(
            remetenteUid = associadoUid,
            remetenteTipo = associadoTipo.name,
            destinatarioUid = pacienteUid
        )
        firestore.collection("solicitacoesVinculo").add(solicitacao).await()
    }

    suspend fun getSolicitacoesPendentes(uid: String): List<SolicitacaoVinculo> {
        val querySnapshot = firestore.collection("solicitacoesVinculo")
            .whereEqualTo("destinatarioUid", uid)
            .get()
            .await()
        return querySnapshot.toObjects(SolicitacaoVinculo::class.java)
    }

    suspend fun aceitarVinculacao(solicitacao: SolicitacaoVinculo) {
        val pacienteDocRef = firestore.collection(FirestoreCollections.PACIENTES).document(solicitacao.destinatarioUid)

        val campoParaAtualizar = when (solicitacao.remetenteTipo) {
            "CUIDADOR" -> "cuidadoresIds"
            "PROFISSIONAL_SAUDE" -> "profissionaisIds"
            else -> throw Exception("Tipo de perfil inválido para vinculação.")
        }

        pacienteDocRef.update(campoParaAtualizar, FieldValue.arrayUnion(solicitacao.remetenteUid)).await()

        firestore.collection("solicitacoesVinculo").document(solicitacao.id).delete().await()
    }

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

    suspend fun getUsuarioByUid(uid: String, tipo: TipoUsuario): Usuario? {
        val collectionPath = when (tipo) {
            TipoUsuario.PACIENTE -> FirestoreCollections.PACIENTES
            TipoUsuario.CUIDADOR -> FirestoreCollections.CUIDADORES
            TipoUsuario.PROFISSIONAL_SAUDE -> FirestoreCollections.PROFISSIONAIS_DA_SAUDE
        }
        val userDoc = firestore.collection(collectionPath).document(uid).get().await()

        return if (userDoc.exists()) {
            Usuario(
                uid = userDoc.id,
                nome = userDoc.getString("nome") ?: "",
                email = userDoc.getString("email") ?: "",
                tipo = tipo
            )
        } else {
            null
        }
    }

    suspend fun getProfissionaisECuidadoresVinculados(pacienteUid: String): List<Usuario> {
        val pacienteDoc = firestore.collection(FirestoreCollections.PACIENTES).document(pacienteUid).get().await()
        if (!pacienteDoc.exists()) {
            return emptyList()
        }

        val profissionaisIds = pacienteDoc["profissionaisIds"] as? List<String> ?: emptyList()
        val cuidadoresIds = pacienteDoc["cuidadoresIds"] as? List<String> ?: emptyList()

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

    suspend fun getMedicosVinculados(uid: String, tipo: TipoUsuario): List<Medico> {
        if (tipo != TipoUsuario.PACIENTE) {
            return emptyList()
        }

        val pacienteDoc = firestore.collection(FirestoreCollections.PACIENTES).document(uid).get().await()
        if (!pacienteDoc.exists()) {
            return emptyList()
        }

        val profissionaisIds = pacienteDoc["profissionaisIds"] as? List<String> ?: emptyList()

        if (profissionaisIds.isEmpty()) {
            return emptyList()
        }

        val medicosQuery = firestore.collection(FirestoreCollections.PROFISSIONAIS_DA_SAUDE)
            .whereIn("uid", profissionaisIds)
            .get()
            .await()

        return medicosQuery.documents.mapNotNull { doc ->
            doc.toObject(Medico::class.java)
        }
    }

    suspend fun prescreverMedicacao(
        pacienteUid: String,
        nome: String,
        dosagem: String,
        frequencia: String,
        duracao: String,
        observacoes: String,
        arquivoUri: Uri?
    ) {
        val medicacaoRef = firestore.collection(FirestoreCollections.MEDICACOES).document()
        val arquivoUrl = uploadFile(medicacaoRef.id, "medicacoes_arquivos", arquivoUri)

        val medicacao = Medicacao(
            id = medicacaoRef.id,
            pacienteUid = pacienteUid,
            nome = nome,
            dosagem = dosagem,
            frequencia = frequencia,
            duracao = duracao,
            observacoes = observacoes,
            arquivoUrl = arquivoUrl
        )
        medicacaoRef.set(medicacao).await()
    }

    suspend fun salvarExame(pacienteUid: String, examePedido: String, unidade: String, diagnostico: String, arquivoUri: Uri?) {
        val exameRef = firestore.collection(FirestoreCollections.EXAMES).document()
        val arquivoUrl = uploadFile(exameRef.id, "exames_arquivos", arquivoUri)
        val exame = Exame(
            id = exameRef.id,
            pacienteUid = pacienteUid,
            examePedido = examePedido,
            unidade = unidade,
            diagnostico = diagnostico,
            arquivoUrl = arquivoUrl
        )
        exameRef.set(exame).await()
    }

    suspend fun salvarVacinacao(pacienteUid: String, vacina1: String, vacina2: String, vacina3: String, vacina4: String, arquivoUri: Uri?) {
        val vacinacaoRef = firestore.collection(FirestoreCollections.VACINACAO).document()
        val arquivoUrl = uploadFile(vacinacaoRef.id, "vacinacao_arquivos", arquivoUri)
        val vacinacao = Vacinacao(
            id = vacinacaoRef.id,
            pacienteUid = pacienteUid,
            vacina1 = vacina1,
            vacina2 = vacina2,
            vacina3 = vacina3,
            vacina4 = vacina4,
            arquivoUrl = arquivoUrl
        )
        vacinacaoRef.set(vacinacao).await()
    }

    suspend fun salvarInternacao(pacienteUid: String, unidade: String, motivo: String, data: String, arquivoUri: Uri?) {
        val internacaoRef = firestore.collection(FirestoreCollections.INTERNACOES).document()
        val arquivoUrl = uploadFile(internacaoRef.id, "internacoes_arquivos", arquivoUri)
        val internacao = Internacao(
            id = internacaoRef.id,
            pacienteUid = pacienteUid,
            unidade = unidade,
            motivo = motivo,
            data = data,
            arquivoUrl = arquivoUrl
        )
        internacaoRef.set(internacao).await()
    }

    suspend fun salvarFisioterapia(pacienteUid: String, data: String, local: String, sessoes: String, diagnostico: String, arquivoUri: Uri?) {
        val fisioterapiaRef = firestore.collection(FirestoreCollections.FISIOTERAPIA).document()
        val arquivoUrl = uploadFile(fisioterapiaRef.id, "fisioterapia_arquivos", arquivoUri)
        val fisioterapia = Fisioterapia(
            id = fisioterapiaRef.id,
            pacienteUid = pacienteUid,
            data = data,
            local = local,
            sessoes = sessoes,
            diagnostico = diagnostico,
            arquivoUrl = arquivoUrl
        )
        fisioterapiaRef.set(fisioterapia).await()
    }

    suspend fun salvarSaudeMental(pacienteUid: String, unidade: String, tratamento: String, data: String, duracao: String, arquivoUri: Uri?) {
        val saudeMentalRef = firestore.collection(FirestoreCollections.SAUDE_MENTAL).document()
        val arquivoUrl = uploadFile(saudeMentalRef.id, "saude_mental_arquivos", arquivoUri)
        val saudeMental = SaudeMental(
            id = saudeMentalRef.id,
            pacienteUid = pacienteUid,
            unidade = unidade,
            tratamento = tratamento,
            data = data,
            duracao = duracao,
            arquivoUrl = arquivoUrl
        )
        saudeMentalRef.set(saudeMental).await()
    }

    suspend fun salvarNutricao(pacienteUid: String, data: String, local: String, diagnostico: String, arquivoUri: Uri?) {
        val nutricaoRef = firestore.collection(FirestoreCollections.NUTRICAO).document()
        val arquivoUrl = uploadFile(nutricaoRef.id, "nutricao_arquivos", arquivoUri)
        val nutricao = Nutricao(
            id = nutricaoRef.id,
            pacienteUid = pacienteUid,
            data = data,
            local = local,
            diagnostico = diagnostico,
            arquivoUrl = arquivoUrl
        )
        nutricaoRef.set(nutricao).await()
    }

    suspend fun getMedicacoes(pacienteUid: String): List<Medicacao> {
        val querySnapshot = firestore.collection(FirestoreCollections.MEDICACOES)
            .whereEqualTo("pacienteUid", pacienteUid)
            .orderBy("dataPrescricao", Query.Direction.DESCENDING)
            .get()
            .await()

        return querySnapshot.toObjects(Medicacao::class.java)
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