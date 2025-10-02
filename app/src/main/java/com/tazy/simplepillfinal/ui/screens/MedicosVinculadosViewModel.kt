package com.tazy.simplepillfinal.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tazy.simplepillfinal.data.AuthRepository
import com.tazy.simplepillfinal.model.Usuario
import kotlinx.coroutines.launch

class MedicosVinculadosViewModel : ViewModel() {
    private val authRepository: AuthRepository = AuthRepository()

    var medicos by mutableStateOf<List<Usuario>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var selectedMedico by mutableStateOf<Usuario?>(null)
        private set

    var showPasswordDialog by mutableStateOf(false)
        private set

    fun carregarMedicosVinculados(pacienteUid: String) {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            try {
                val paciente = authRepository.getPaciente(pacienteUid)
                if (paciente != null) {
                    val medicosIds = paciente.profissionaisIds
                    val medicosList = medicosIds.mapNotNull { id ->
                        authRepository.getUsuario(id)
                    }
                    medicos = medicosList
                } else {
                    errorMessage = "Paciente não encontrado."
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha ao carregar médicos vinculados."
            } finally {
                isLoading = false
            }
        }
    }

    fun selecionarMedico(medico: Usuario) {
        selectedMedico = medico
    }

    fun desfazerVinculo(pacienteUid: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                selectedMedico?.let { medico ->
                    authRepository.desfazerVinculo(
                        pacienteUid = pacienteUid,
                        profissionalUid = medico.uid,
                        password = password
                    )
                    // Atualiza a lista na UI após a exclusão
                    medicos = medicos.filter { it.uid != medico.uid }
                    // Navega de volta após o sucesso
                    selectedMedico = null
                    showPasswordDialog = false
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha ao desfazer vínculo."
            } finally {
                isLoading = false
            }
        }
    }

    fun clearSelection() {
        selectedMedico = null
    }

    fun togglePasswordDialog(show: Boolean) {
        showPasswordDialog = show
    }
}