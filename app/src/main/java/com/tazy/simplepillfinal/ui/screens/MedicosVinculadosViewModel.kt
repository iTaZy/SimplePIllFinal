// C:/Users/amigo/StudioProjects/SimplePIllFinal/app/src/main/java/com/tazy/simplepillfinal/ui/screens/MedicosVinculadosViewModel.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tazy.simplepillfinal.data.AuthRepository
import com.tazy.simplepillfinal.model.Medico // Assuming you have a Medico model
import com.tazy.simplepillfinal.model.TipoUsuario
import kotlinx.coroutines.launch

// --- FIX: Rename the class to match the file name ---
class MedicosVinculadosViewModel : ViewModel() {
    private val authRepository: AuthRepository = AuthRepository()

    // --- FIX: Adjust variables to refer to "Medicos" ---
    var medicos by mutableStateOf<List<Medico>>(emptyList()) // Changed from Paciente to Medico
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // --- FIX: Adjust function and logic to load "Medicos" ---
    fun carregarMedicos(uid: String, tipo: TipoUsuario) { // Renamed function
        isLoading = true
        viewModelScope.launch {
            try {
                // You'll likely need a method like getMedicosVinculados in your repository
                medicos = authRepository.getMedicosVinculados(uid, tipo)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Falha ao carregar médicos." // Updated error message
            } finally {
                isLoading = false
            }
        }
    }
}
