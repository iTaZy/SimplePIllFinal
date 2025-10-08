// F_ARQUIVO: itazy/simplepillfinal/SimplePIllFinal-23165cb55ae68d55ff279be231b459964f532606/app/src/main/java/com/tazy/simplepillfinal/ui/screens/PrescreverMedicacaoViewModel.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tazy.simplepillfinal.data.AuthRepository
import kotlinx.coroutines.launch
import android.net.Uri // Importar a classe Uri

class PrescreverMedicacaoViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    var nome by mutableStateOf("")
    var dosagem by mutableStateOf("")
    var frequencia by mutableStateOf("")
    var duracao by mutableStateOf("")
    var observacoes by mutableStateOf("")
    var arquivoUri by mutableStateOf<Uri?>(null) // Novo estado para o URI do arquivo

    var isLoading by mutableStateOf(false)
        private set
    var prescriptionSuccess by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onPrescribeClick(pacienteUid: String) {
        if (nome.isBlank()) {
            errorMessage = "O nome da medicação é obrigatório."
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                authRepository.prescreverMedicacao(
                    pacienteUid = pacienteUid,
                    nome = nome,
                    dosagem = dosagem,
                    frequencia = frequencia,
                    duracao = duracao,
                    observacoes = observacoes,
                    arquivoUri = arquivoUri // Passar o URI do arquivo
                )
                prescriptionSuccess = true
            } catch (e: Exception) {
                errorMessage = e.message ?: "Ocorreu um erro ao salvar a prescrição."
            } finally {
                isLoading = false
            }
        }
    }
}