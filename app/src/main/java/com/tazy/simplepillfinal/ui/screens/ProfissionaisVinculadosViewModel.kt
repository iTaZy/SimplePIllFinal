// CRIE ESTE NOVO ARQUIVO: ui/screens/ProfissionaisVinculadosViewModel.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tazy.simplepillfinal.data.AuthRepository
import com.tazy.simplepillfinal.model.Usuario
import kotlinx.coroutines.launch

class ProfissionaisVinculadosViewModel : ViewModel() {
    private val authRepository: AuthRepository = AuthRepository()

    var vinculados by mutableStateOf<List<Usuario>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun carregarVinculados(pacienteUid: String) {
        isLoading = true
        viewModelScope.launch {
            try {
                vinculados = authRepository.getProfissionaisECuidadoresVinculados(pacienteUid)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha ao carregar profissionais e cuidadores vinculados."
            } finally {
                isLoading = false
            }
        }
    }
}