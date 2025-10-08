// F_ARQUIVO: ui/screens/CadastroGeralViewModel.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tazy.simplepillfinal.data.AuthRepository
import com.tazy.simplepillfinal.model.TipoUsuario
import kotlinx.coroutines.launch

class CadastroGeralViewModel : ViewModel() {
    private val authRepository: AuthRepository = AuthRepository()

    var perfisSelecionados by mutableStateOf(setOf<TipoUsuario>())
    fun togglePerfil(tipo: TipoUsuario) {
        perfisSelecionados = if (tipo in perfisSelecionados) {
            perfisSelecionados - tipo
        } else {
            perfisSelecionados + tipo
        }
    }

    var nome by mutableStateOf("")
    var email by mutableStateOf("")
    var senha by mutableStateOf("")
    var telefone by mutableStateOf("")
    var idade by mutableStateOf("")
    var endereco by mutableStateOf("")
    var profissao by mutableStateOf("")

    var nacionalidade by mutableStateOf("")
    var numSus by mutableStateOf("")
    var unidadeSus by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var registrationSuccess by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    // Funções de filtro de entrada
    fun onNameChange(input: String) {
        val filteredInput = input.filter { it.isLetter() || it.isWhitespace() }
        if (filteredInput.length <= 50) { // Limita o tamanho para evitar inputs muito longos
            nome = filteredInput
        }
    }

    fun onAgeChange(input: String) {
        val filteredInput = input.filter { it.isDigit() }
        if (filteredInput.length <= 3) { // Limita a idade para 3 dígitos
            idade = filteredInput
        }
    }

    fun onPhoneChange(input: String) {
        val filteredInput = input.filter { it.isDigit() }
        if (filteredInput.length <= 15) { // Limita o telefone para 15 dígitos
            telefone = filteredInput
        }
    }

    fun onNationalityChange(input: String) {
        val filteredInput = input.filter { it.isLetter() || it.isWhitespace() }
        if (filteredInput.length <= 50) {
            nacionalidade = filteredInput
        }
    }

    fun onSusChange(input: String) {
        val filteredInput = input.filter { it.isDigit() }
        if (filteredInput.length <= 16) { // Limita o SUS para 16 dígitos
            numSus = filteredInput
        }
    }


    // Funções de validação antes do cadastro
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        val hasUppercase = password.any { it.isUpperCase() }
        return password.length >= 6 && hasUppercase
    }

    fun onRegistrationClick() {
        errorMessage = null

        if (nome.isBlank() || email.isBlank() || senha.isBlank() || perfisSelecionados.isEmpty()) {
            errorMessage = "Nome, email, senha e ao menos um perfil são obrigatórios."
            return
        }

        if (!isValidEmail(email)) {
            errorMessage = "Por favor, insira um e-mail válido."
            return
        }
        if (!isValidPassword(senha)) {
            errorMessage = "A senha deve ter no mínimo 6 caracteres e uma letra maiúscula."
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                val userDataPorPerfil = mutableMapOf<TipoUsuario, Map<String, Any>>()
                val commonData = hashMapOf(
                    "nome" to nome,
                    "telefone" to telefone,
                    "idade" to idade,
                    "endereco" to endereco,
                    "profissao" to profissao
                )

                if (TipoUsuario.PACIENTE in perfisSelecionados) {
                    val pacienteData = commonData.toMutableMap()
                    pacienteData["nacionalidade"] = nacionalidade
                    pacienteData["numSus"] = numSus
                    pacienteData["unidadeSus"] = unidadeSus
                    userDataPorPerfil[TipoUsuario.PACIENTE] = pacienteData
                }
                if (TipoUsuario.CUIDADOR in perfisSelecionados) {
                    val cuidadorData = commonData.toMutableMap()
                    userDataPorPerfil[TipoUsuario.CUIDADOR] = cuidadorData
                }
                if (TipoUsuario.PROFISSIONAL_SAUDE in perfisSelecionados) {
                    val profSaudeData = commonData.toMutableMap()
                    userDataPorPerfil[TipoUsuario.PROFISSIONAL_SAUDE] = profSaudeData
                }

                authRepository.criarUsuario(email, senha, userDataPorPerfil)
                registrationSuccess = true
            } catch (e: Exception) {
                errorMessage = e.message ?: "Ocorreu um erro desconhecido."
            } finally {
                isLoading = false
            }
        }
    }
}