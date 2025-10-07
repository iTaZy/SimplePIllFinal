// NOVO ARQUIVO: ui/screens/DetalhesProfissionalViewModel.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tazy.simplepillfinal.data.AuthRepository
import com.tazy.simplepillfinal.model.TipoUsuario
import com.tazy.simplepillfinal.model.Usuario
import kotlinx.coroutines.launch

class DetalhesProfissionalViewModel : ViewModel() {
    private val authRepository: AuthRepository = AuthRepository()

    var profissional by mutableStateOf<Usuario?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var showPasswordDialog by mutableStateOf(false)
        private set

    fun carregarProfissional(profissionalUid: String, tipo: TipoUsuario) {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            try {
                profissional = authRepository.getUsuarioByUid(profissionalUid, tipo)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha ao carregar os dados do profissional."
            } finally {
                isLoading = false
            }
        }
    }

    fun desfazerVinculo(pacienteUid: String, medicoUid: String, password: String) {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            try {
                // Re-autentica o usuário
                authRepository.reauthenticateUser(password)
                // Desvincula o profissional
                authRepository.desvincularMedico(pacienteUid, medicoUid)
                showPasswordDialog = false
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha ao desvincular. Verifique sua senha."
            } finally {
                isLoading = false
            }
        }
    }

    fun togglePasswordDialog(show: Boolean) {
        showPasswordDialog = show
    }
}