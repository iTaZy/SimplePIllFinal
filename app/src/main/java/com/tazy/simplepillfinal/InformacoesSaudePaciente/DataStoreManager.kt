package com.tazy.simplepillfinal.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val NAME = "exercicios_paciente_ds"

// extensão para obter o DataStore<Preferences>
val Context.dataStore by preferencesDataStore(name = NAME)

class DataStoreManager(private val context: Context) {
    private val json = Json { }

    // retorna a Flow<List<String>> dos exercícios daquele dia
    fun getExercicios(dia: String): Flow<List<String>> {
        val key = stringPreferencesKey("exercicios_$dia")
        return context.dataStore.data
            .map { prefs ->
                prefs[key]?.let { json.decodeFromString(it) }
                    ?: emptyList()
            }
    }

    // salva a lista inteira no DataStore
    private suspend fun saveList(dia: String, lista: List<String>) {
        val key = stringPreferencesKey("exercicios_$dia")
        context.dataStore.edit { prefs ->
            prefs[key] = json.encodeToString(lista)
        }
    }

    // adiciona, sem duplicar, e retorna true se foi adicionado
    suspend fun addExercicio(dia: String, exercicio: String): Boolean {
        val current = getExercicios(dia).first()
        if (current.contains(exercicio)) return false
        val updated = current + exercicio
        saveList(dia, updated)
        return true
    }

    // remove e salva
    suspend fun removeExercicio(dia: String, exercicio: String) {
        val current = getExercicios(dia).first()
        saveList(dia, current - exercicio)
    }
}
