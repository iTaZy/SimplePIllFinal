// F_ARQUIVO: ui/screens/LoginViewModel.kt
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

class LoginViewModel : ViewModel() {
    private val authRepository: AuthRepository = AuthRepository()

    var email by mutableStateOf("")
    var senha by mutableStateOf("")

    // NOVO: Estado para o tipo de usuário selecionado
    var tipoUsuario by mutableStateOf(TipoUsuario.PACIENTE)

    var isLoading by mutableStateOf(false)
        private set

    var loginSuccessUser by mutableStateOf<Usuario?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onLoginClick() {
        errorMessage = null // Limpa a mensagem de erro anterior

        if (email.isBlank() || senha.isBlank()) {
            errorMessage = "Por favor, preencha todos os campos."
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                // ATUALIZAÇÃO: Passamos o tipo de usuário para a função
                loginSuccessUser = authRepository.autenticar(email, senha, tipoUsuario)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Ocorreu um erro desconhecido."
            } finally {
                isLoading = false
            }
        }
    }
}