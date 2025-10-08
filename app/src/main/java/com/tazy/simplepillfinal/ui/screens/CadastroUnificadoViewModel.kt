// F_ARQUIVO: itazy/simplepillfinal/SimplePIllFinal-23165cb55ae68d55ff279be231b459964f532606/app/src/main/java/com/tazy/simplepillfinal/ui/screens/CadastroUnificadoViewModel.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tazy.simplepillfinal.data.AuthRepository
import kotlinx.coroutines.launch
import android.net.Uri

class CadastroUnificadoViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    // Estados para os campos de Exame
    var exame by mutableStateOf("")
    var unidadeExame by mutableStateOf("")
    var diagnosticoExame by mutableStateOf("")

    // Estados para os campos de Vacinação
    var vacina1 by mutableStateOf("")
    var vacina2 by mutableStateOf("")
    var vacina3 by mutableStateOf("")
    var vacina4 by mutableStateOf("")

    // Estados para os campos de Internação
    var unidadeInternacao by mutableStateOf("")
    var motivoInternacao by mutableStateOf("")
    var dataInternacao by mutableStateOf("")

    // Estados para os campos de Fisioterapia
    var dataFisioterapia by mutableStateOf("")
    var localFisioterapia by mutableStateOf("")
    var sessoesFisioterapia by mutableStateOf("")
    var diagnosticoFisioterapia by mutableStateOf("")

    // Estados para os campos de Saúde Mental
    var unidadeSaudeMental by mutableStateOf("")
    var tratamentoSaudeMental by mutableStateOf("")
    var dataSaudeMental by mutableStateOf("")
    var duracaoSaudeMental by mutableStateOf("")

    // Estados para os campos de Nutrição
    var dataNutricao by mutableStateOf("")
    var localNutricao by mutableStateOf("")
    var diagnosticoNutricao by mutableStateOf("")

    // NOVO: Estado para o URI do arquivo PDF
    var arquivoUri by mutableStateOf<Uri?>(null)

    var isLoading by mutableStateOf(false)
        private set
    var saveSuccess by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onSaveClick(pacienteUid: String, acao: String) {
        isLoading = true
        viewModelScope.launch {
            try {
                when (acao) {
                    "Exames" -> authRepository.salvarExame(pacienteUid, exame, unidadeExame, diagnosticoExame, arquivoUri)
                    "Vacinação" -> authRepository.salvarVacinacao(pacienteUid, vacina1, vacina2, vacina3, vacina4, arquivoUri)
                    "Internação" -> authRepository.salvarInternacao(pacienteUid, unidadeInternacao, motivoInternacao, dataInternacao, arquivoUri)
                    "Fisioterapia" -> authRepository.salvarFisioterapia(pacienteUid, dataFisioterapia, localFisioterapia, sessoesFisioterapia, diagnosticoFisioterapia, arquivoUri)
                    "Saúde mental" -> authRepository.salvarSaudeMental(pacienteUid, unidadeSaudeMental, tratamentoSaudeMental, dataSaudeMental, duracaoSaudeMental, arquivoUri)
                    "Nutrição" -> authRepository.salvarNutricao(pacienteUid, dataNutricao, localNutricao, diagnosticoNutricao, arquivoUri)
                    else -> errorMessage = "Ação de cadastro desconhecida."
                }
                if (errorMessage == null) {
                    saveSuccess = true
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Ocorreu um erro ao salvar o registro."
            } finally {
                isLoading = false
            }
        }
    }

    fun clearState() {
        saveSuccess = false
        errorMessage = null
        isLoading = false
        // Limpar o URI do arquivo
        arquivoUri = null
    }
}