package com.tazy.simplepillfinal.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tazy.simplepillfinal.data.AuthRepository
import com.tazy.simplepillfinal.model.SolicitacaoVinculacao
import com.tazy.simplepillfinal.model.TipoUsuario
import kotlinx.coroutines.launch

class ConfirmacoesViewModel : ViewModel() {
    private val authRepository: AuthRepository = AuthRepository()

    var solicitacoes by mutableStateOf<List<SolicitacaoVinculacao>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun carregarSolicitacoes(pacienteUid: String) {
        isLoading = true
        viewModelScope.launch {
            try {
                solicitacoes = authRepository.getSolicitacoesPendentes(pacienteUid)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha ao carregar solicitações."
            } finally {
                isLoading = false
            }
        }
    }

    fun aceitarSolicitacao(pacienteUid: String, solicitanteUid: String, tipo: TipoUsuario) {
        viewModelScope.launch {
            try {
                authRepository.aceitarVinculacao(pacienteUid, solicitanteUid, tipo)
                // Atualiza a lista após a ação
                solicitacoes = solicitacoes.filter { it.solicitanteUid != solicitanteUid }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha ao aceitar solicitação."
            }
        }
    }

    fun negarSolicitacao(pacienteUid: String, solicitanteUid: String, tipo: TipoUsuario) {
        viewModelScope.launch {
            try {
                authRepository.negarVinculacao(pacienteUid, solicitanteUid, tipo)
                // Atualiza a lista após a ação
                solicitacoes = solicitacoes.filter { it.solicitanteUid != solicitanteUid }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha ao negar solicitação."
            }
        }
    }
}