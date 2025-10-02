// NOVO ARQUIVO: ui/screens/ConfirmacoesViewModel.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tazy.simplepillfinal.data.AuthRepository
import com.tazy.simplepillfinal.model.SolicitacaoVinculo
import kotlinx.coroutines.launch

class ConfirmacoesViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    var solicitacoes by mutableStateOf<List<SolicitacaoVinculo>>(emptyList())
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
                errorMessage = e.message ?: "Falha ao carregar as solicitações."
            } finally {
                isLoading = false
            }
        }
    }

    fun aceitarVinculacao(solicitacao: SolicitacaoVinculo) {
        viewModelScope.launch {
            try {
                authRepository.aceitarVinculacao(solicitacao)
                solicitacoes = solicitacoes.filter { it.id != solicitacao.id }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha ao aceitar a solicitação."
            }
        }
    }

    fun negarVinculacao(solicitacao: SolicitacaoVinculo) {
        viewModelScope.launch {
            try {
                authRepository.negarVinculacao(solicitacao.id)
                solicitacoes = solicitacoes.filter { it.id != solicitacao.id }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha ao negar a solicitação."
            }
        }
    }
}