// CRIE ESTE NOVO ARQUIVO: ui/screens/LinkPacienteViewModel.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tazy.simplepillfinal.data.AuthRepository
import com.tazy.simplepillfinal.model.TipoUsuario
import kotlinx.coroutines.launch

class LinkPacienteViewModel : ViewModel() {
    private val authRepository: AuthRepository = AuthRepository()

    var emailPaciente by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set
    var linkSuccess by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onLinkPatientClick(associadoUid: String, associadoTipo: TipoUsuario) {
        if (emailPaciente.isBlank()) {
            errorMessage = "Por favor, insira o e-mail do paciente."
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                // CORREÇÃO: Chamando a nova função para enviar a solicitação
                authRepository.enviarSolicitacaoVinculacao(
                    emailPaciente = emailPaciente,
                    associadoUid = associadoUid,
                    associadoTipo = associadoTipo
                )
                linkSuccess = true
            } catch (e: Exception) {
                errorMessage = e.message ?: "Não foi possível vincular o paciente."
            } finally {
                isLoading = false
            }
        }
    }
}