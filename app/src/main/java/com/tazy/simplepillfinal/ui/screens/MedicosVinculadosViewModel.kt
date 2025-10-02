// C:/Users/amigo/StudioProjects/SimplePIllFinal/app/src/main/java/com/tazy/simplepillfinal/ui/screens/MedicosVinculadosViewModel.kt
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

    // --- NEW: State for the selected doctor details screen ---
    var selectedMedico by mutableStateOf<Medico?>(null)
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

    // --- NEW: Function to set the selected doctor and navigate ---
    fun selecionarMedico(medico: Medico) {
        selectedMedico = medico
    }

    // --- NEW: Function to toggle the password confirmation dialog ---
    fun togglePasswordDialog(show: Boolean) {
        showPasswordDialog = show
    }

    // --- NEW: Function to unlink a doctor after password confirmation ---
    fun desfazerVinculo(pacienteUid: String, password: String) {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            try {
                // Re-authenticate user
                authRepository.reauthenticateUser(password)
                // Unlink doctor
                authRepository.desvincularMedico(pacienteUid, selectedMedico!!.id)
                // Reload list and clear state
                carregarMedicosVinculados(pacienteUid)
                showPasswordDialog = false
                selectedMedico = null
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha ao desvincular. Verifique sua senha."
            } finally {
                isLoading = false
            }
        }
    }
}