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

    // ATUALIZAÇÃO: Estado para guardar um CONJUNTO de perfis
    var perfisSelecionados by mutableStateOf(setOf<TipoUsuario>())
    // Adicionamos a função para adicionar ou remover perfis do conjunto
    fun togglePerfil(tipo: TipoUsuario) {
        perfisSelecionados = if (tipo in perfisSelecionados) {
            perfisSelecionados - tipo
        } else {
            perfisSelecionados + tipo
        }
    }

    // Campos Comuns (aqui ficarão os campos que todos os usuários têm)
    var nome by mutableStateOf("")
    var email by mutableStateOf("")
    var senha by mutableStateOf("")
    var telefone by mutableStateOf("")
    var idade by mutableStateOf("")
    var endereco by mutableStateOf("")

    // Campos Específicos de Paciente
    var nacionalidade by mutableStateOf("")
    var numSus by mutableStateOf("")
    var unidadeSus by mutableStateOf("")

    // Campos Específicos de Cuidador/Profissional
    var profissao by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var registrationSuccess by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun onRegistrationClick() {
        if (nome.isBlank() || email.isBlank() || senha.isBlank() || perfisSelecionados.isEmpty()) {
            errorMessage = "Nome, email, senha e ao menos um perfil são obrigatórios."
            return
        }
        isLoading = true
        viewModelScope.launch {
            try {
                // ATUALIZAÇÃO: Monta um mapa de dados para cada perfil selecionado
                val userDataPorPerfil = mutableMapOf<TipoUsuario, Map<String, Any>>()

                // Dados comuns a todos os perfis
                val commonData = hashMapOf(
                    "nome" to nome,
                    "telefone" to telefone,
                    "idade" to idade,
                    "endereco" to endereco,
                    "profissao" to profissao // Profissão pode ser comum ou específica
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
                    // Adicione campos específicos do cuidador aqui, se houver
                    userDataPorPerfil[TipoUsuario.CUIDADOR] = cuidadorData
                }

                if (TipoUsuario.PROFISSIONAL_SAUDE in perfisSelecionados) {
                    val profSaudeData = commonData.toMutableMap()
                    // Adicione campos específicos do profissional aqui, se houver
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