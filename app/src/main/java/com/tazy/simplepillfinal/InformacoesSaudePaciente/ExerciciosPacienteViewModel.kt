package com.tazy.simplepillfinal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tazy.simplepillfinal.data.DataStoreManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ExerciciosDataStoreViewModel(
    private val dsManager: DataStoreManager
) : ViewModel() {

    // expõe a lista como StateFlow
    private val _lista = MutableStateFlow<List<String>>(emptyList())
    val lista: StateFlow<List<String>> = _lista.asStateFlow()

    fun load(dia: String) {
        viewModelScope.launch {
            dsManager.getExercicios(dia)
                .collect { _lista.value = it }
        }
    }

    fun add(dia: String, exercicio: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val ok = dsManager.addExercicio(dia, exercicio)
            onResult(ok)
        }
    }

    fun remove(dia: String, exercicio: String) {
        viewModelScope.launch {
            dsManager.removeExercicio(dia, exercicio)
        }
    }
}
