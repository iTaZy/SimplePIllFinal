// F_ARQUIVO: ui/screens/DetalhesRegistroViewModel.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tazy.simplepillfinal.data.AuthRepository
import com.tazy.simplepillfinal.model.*
import kotlinx.coroutines.launch

class DetalhesRegistroViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    var registro by mutableStateOf<Any?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun carregarRegistro(tipoAcao: String, registroId: String) {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            try {
                registro = when (tipoAcao) {
                    "Medicações" -> authRepository.getMedicacaoById(registroId)
                    "Exames" -> authRepository.getExameById(registroId)
                    "Vacinação" -> authRepository.getVacinacaoById(registroId)
                    "Internações" -> authRepository.getInternacaoById(registroId)
                    "Fisioterapia" -> authRepository.getFisioterapiaById(registroId)
                    "Saúde Mental" -> authRepository.getSaudeMentalById(registroId)
                    "Nutrição" -> authRepository.getNutricaoById(registroId)
                    else -> null
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha ao carregar os detalhes do registro."
            } finally {
                isLoading = false
            }
        }
    }
}