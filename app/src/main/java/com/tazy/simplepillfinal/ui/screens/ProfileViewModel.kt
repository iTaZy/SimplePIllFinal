// ARQUIVO CORRIGIDO: ui/screens/ProfileViewModel.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tazy.simplepillfinal.data.AuthRepository
import com.tazy.simplepillfinal.model.Cuidador
import com.tazy.simplepillfinal.model.Paciente
import com.tazy.simplepillfinal.model.ProfissionalSaude
import com.tazy.simplepillfinal.model.TipoUsuario
import kotlinx.coroutines.launch
import com.tazy.simplepillfinal.R

class ProfileViewModel : ViewModel() {
    private val authRepository: AuthRepository = AuthRepository()

    var user by mutableStateOf<Any?>(null)
        private set
    var nome by mutableStateOf("")
    var email by mutableStateOf("")
    var telefone by mutableStateOf("")
    var endereco by mutableStateOf("")
    var nationalId by mutableStateOf("") // Usado para CPF ou CRM
    var susCard by mutableStateOf("")
    var ufCrm by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set
    var saveSuccess by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    var showPasswordDialog by mutableStateOf(false)
    var currentPassword by mutableStateOf("")

    fun loadUser(uid: String, tipo: TipoUsuario) {
        isLoading = true
        viewModelScope.launch {
            try {
                when (tipo) {
                    TipoUsuario.PACIENTE -> {
                        val paciente = authRepository.getPacienteByUid(uid)
                        user = paciente
                        paciente?.let {
                            nome = it.nome
                            email = it.email
                            telefone = it.telefone
                            endereco = it.endereco
                            nationalId = it.cpf // CORRIGIDO: Carregando 'cpf'
                            susCard = it.numSus
                        }
                    }
                    TipoUsuario.CUIDADOR -> {
                        val cuidador = authRepository.getCuidadorByUid(uid)
                        user = cuidador
                        cuidador?.let {
                            nome = it.nome
                            email = it.email
                            telefone = it.telefone
                            endereco = it.endereco
                            nationalId = it.cpf
                        }
                    }
                    TipoUsuario.PROFISSIONAL_SAUDE -> {
                        val profissional = authRepository.getProfissionalSaudeByUid(uid)
                        user = profissional
                        profissional?.let {
                            nome = it.nome
                            email = it.email
                            telefone = it.telefone
                            endereco = it.endereco
                            nationalId = it.crm
                            ufCrm = it.ufCrm
                        }
                    }
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha ao carregar os dados do usuário."
            } finally {
                isLoading = false
            }
        }
    }

    fun onSaveClick(uid: String, tipo: TipoUsuario) {
        isLoading = true
        viewModelScope.launch {
            try {
                val currentUserEmail = authRepository.getEmailByUid(uid, tipo)
                if (email != currentUserEmail) {
                    showPasswordDialog = true
                } else {
                    authRepository.updateUser(uid, tipo, email, telefone, endereco, nationalId, susCard, ufCrm)
                    saveSuccess = true
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha ao salvar o perfil."
            } finally {
                isLoading = false
            }
        }
    }

    fun onConfirmPasswordAndSave(uid: String, tipo: TipoUsuario) {
        isLoading = true
        viewModelScope.launch {
            try {
                authRepository.reauthenticateUser(currentPassword)
                authRepository.updateUser(uid, tipo, email, telefone, endereco, nationalId, susCard, ufCrm)
                showPasswordDialog = false
                saveSuccess = true
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha na reautenticação. Verifique sua senha."
            } finally {
                isLoading = false
                currentPassword = ""
            }
        }
    }

    fun clearState() {
        saveSuccess = false
        errorMessage = null
        isLoading = false
        showPasswordDialog = false
    }
}