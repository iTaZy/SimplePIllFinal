// CRIE ESTE NOVO ARQUIVO: ui/screens/MinhasMedicacoesViewModel.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tazy.simplepillfinal.data.AuthRepository
import com.tazy.simplepillfinal.model.Medicacao
import kotlinx.coroutines.launch

class MinhasMedicacoesViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    var medicacoes by mutableStateOf<List<Medicacao>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun carregarMedicacoes(pacienteUid: String) {
        isLoading = true
        viewModelScope.launch {
            try {
                medicacoes = authRepository.getMedicacoes(pacienteUid)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha ao carregar as medicações."
            } finally {
                isLoading = false
            }
        }
    }
}