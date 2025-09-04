// CRIE ESTE NOVO ARQUIVO: ui/screens/VisualizacaoGeralViewModel.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tazy.simplepillfinal.data.AuthRepository
import com.tazy.simplepillfinal.model.*
import kotlinx.coroutines.launch

class VisualizacaoGeralViewModel : ViewModel() {
    private val authRepository: AuthRepository = AuthRepository()

    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Estados para cada tipo de lista de dados
    var exames by mutableStateOf<List<Exame>>(emptyList())
        private set
    var vacinacao by mutableStateOf<List<Vacinacao>>(emptyList())
        private set
    var internacoes by mutableStateOf<List<Internacao>>(emptyList())
        private set
    var fisioterapia by mutableStateOf<List<Fisioterapia>>(emptyList())
        private set
    var saudeMental by mutableStateOf<List<SaudeMental>>(emptyList())
        private set
    var nutricao by mutableStateOf<List<Nutricao>>(emptyList())
        private set

    fun carregarDados(pacienteUid: String, acao: String) {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            try {
                when (acao) {
                    "Exames" -> exames = authRepository.getExames(pacienteUid)
                    "Vacinação" -> vacinacao = authRepository.getVacinacao(pacienteUid)
                    "Internações" -> internacoes = authRepository.getInternacoes(pacienteUid)
                    "Fisioterapia" -> fisioterapia = authRepository.getFisioterapia(pacienteUid)
                    "Saúde Mental" -> saudeMental = authRepository.getSaudeMental(pacienteUid)
                    "Nutrição" -> nutricao = authRepository.getNutricao(pacienteUid)
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha ao carregar os dados de $acao."
            } finally {
                isLoading = false
            }
        }
    }
}