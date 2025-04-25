// MedicacaoPacienteDataStore.kt
package com.tazy.simplepillfinal.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.tazy.simplepillfinal.data.model.Medicacao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.medicacaoDataStore by preferencesDataStore("medicacoes_paciente")

private val KEY_JSON = stringPreferencesKey("medicacoes_json")

class MedicacaoPacienteDataStore(private val context: Context) {

    private val json = Json { encodeDefaults = true }

    private suspend fun writeAll(lista: List<Medicacao>) {
        context.medicacaoDataStore.edit { prefs ->
            prefs[KEY_JSON] = json.encodeToString(lista)
        }
    }

    fun getMedicacoes(): Flow<List<Medicacao>> =
        context.medicacaoDataStore.data
            .map { prefs ->
                prefs[KEY_JSON]
                    ?.let { json.decodeFromString(it) }
                    ?: emptyList()
            }

    suspend fun addMedicacao(m: Medicacao): Boolean {
        val atual = getMedicacoes().first().toMutableList()
        if (m.nome.isBlank() || atual.any { it.nome == m.nome }) return false
        atual += m
        writeAll(atual)
        return true
    }

    suspend fun removeMedicacao(nome: String) {
        val atual = getMedicacoes().first().filterNot { it.nome == nome }
        writeAll(atual)
    }
}
