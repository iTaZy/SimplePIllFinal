package com.tazy.simplepillfinal.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tazy.simplepillfinal.data.MedicacaoPacienteDataStore

class MedicacaoPacienteViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val store = MedicacaoPacienteDataStore(context)
        return MedicacaoPacienteViewModel(store) as T
    }
}
