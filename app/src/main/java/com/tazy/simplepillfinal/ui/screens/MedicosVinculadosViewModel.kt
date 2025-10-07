// F_ARQUIVO: ui/screens/MedicosVinculadosViewModel.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tazy.simplepillfinal.data.AuthRepository
import com.tazy.simplepillfinal.model.Medico
import com.tazy.simplepillfinal.model.TipoUsuario
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth

class MedicosVinculadosViewModel : ViewModel() {
    private val authRepository: AuthRepository = AuthRepository()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var medicos by mutableStateOf<List<Medico>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // --- NEW: State for the password confirmation dialog ---
    var showPasswordDialog by mutableStateOf(false)
        private set

    fun carregarMedicosVinculados(pacienteUid: String) {
        isLoading = true
        viewModelScope.launch {
            try {
                medicos = authRepository.getMedicosVinculados(pacienteUid, TipoUsuario.PACIENTE)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha ao carregar médicos."
            } finally {
                isLoading = false
            }
        }
    }

    // Corrigido: A função `desfazerVinculo` agora recebe o `medicoId` diretamente.
    fun desfazerVinculo(pacienteUid: String, medicoUid: String, password: String) {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            try {
                // Re-authenticate user
                authRepository.reauthenticateUser(password)
                // Unlink doctor
                authRepository.desvincularMedico(pacienteUid, medicoUid)
                // Reload list and clear state
                carregarMedicosVinculados(pacienteUid)
                showPasswordDialog = false
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha ao desvincular. Verifique sua senha."
            } finally {
                isLoading = false
            }
        }
    }

    // --- NEW: Function to toggle the password confirmation dialog ---
    fun togglePasswordDialog(show: Boolean) {
        showPasswordDialog = show
    }
}