// MedicacaoPacienteViewModel.kt
package com.tazy.simplepillfinal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tazy.simplepillfinal.data.MedicacaoPacienteDataStore
import com.tazy.simplepillfinal.data.model.Medicacao
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MedicacaoPacienteViewModel(
    private val store: MedicacaoPacienteDataStore
) : ViewModel() {

    val medicacoes: StateFlow<List<Medicacao>> =
        store.getMedicacoes()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addMedicacao(m: Medicacao, onResult: (Boolean)->Unit) = viewModelScope.launch {
        onResult(store.addMedicacao(m))
    }

    fun removeMedicacao(nome: String) = viewModelScope.launch {
        store.removeMedicacao(nome)
    }
}
