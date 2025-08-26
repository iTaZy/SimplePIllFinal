// F_ARQUIVO: ui/screens/CadastroPacienteViewModel.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tazy.simplepillfinal.data.AuthRepository
import com.tazy.simplepillfinal.model.TipoUsuario
import kotlinx.coroutines.launch

class CadastroPacienteViewModel : ViewModel() {
    private val authRepository: AuthRepository = AuthRepository()

    var nome by mutableStateOf("")
    var email by mutableStateOf("")
    var senha by mutableStateOf("")
    // Adicione outros campos conforme necessário

    var isLoading by mutableStateOf(false)
        private set
    var registrationSuccess by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onRegistrationClick() {
        if (nome.isBlank() || email.isBlank() || senha.isBlank()) {
            errorMessage = "Por favor, preencha todos os campos."
            return
        }
        isLoading = true
        viewModelScope.launch {
            try {
                val pacienteData = hashMapOf("nome" to nome) // Adicione outros dados aqui
                authRepository.criarUsuario(email, senha, TipoUsuario.PACIENTE, pacienteData)
                registrationSuccess = true
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
}