// CRIE ESTE NOVO ARQUIVO: ui/screens/PacientesVinculadosViewModel.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tazy.simplepillfinal.data.AuthRepository
import com.tazy.simplepillfinal.model.Paciente
import com.tazy.simplepillfinal.model.TipoUsuario
import kotlinx.coroutines.launch

class PacientesVinculadosViewModel : ViewModel() {
    private val authRepository: AuthRepository = AuthRepository()

    var pacientes by mutableStateOf<List<Paciente>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun carregarPacientes(uid: String, tipo: TipoUsuario) {
        isLoading = true
        viewModelScope.launch {
            try {
                pacientes = authRepository.getPacientesVinculados(uid, tipo)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha ao carregar pacientes."
            } finally {
                isLoading = false
            }
        }
    }
}