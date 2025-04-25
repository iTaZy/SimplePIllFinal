package com.tazy.simplepillfinal.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.exerciciosDataStore by preferencesDataStore(name = "exercicios_paciente")

class ExerciciosPacienteDataStore(private val context: Context) {

    private fun keyParaDia(dia: String) = stringSetPreferencesKey("exercicios_$dia")

    fun getExercicios(dia: String): Flow<List<String>> {
        return context.exerciciosDataStore.data.map { prefs ->
            prefs[keyParaDia(dia)]?.toList() ?: emptyList()
        }
    }

    suspend fun addExercicio(dia: String, exercicio: String): Boolean {
        var adicionado = false
        context.exerciciosDataStore.edit { prefs ->
            val chave = keyParaDia(dia)
            val listaAtual = prefs[chave]?.toMutableSet() ?: mutableSetOf()
            if (!listaAtual.contains(exercicio)) {
                listaAtual.add(exercicio)
                prefs[chave] = listaAtual
                adicionado = true
            }
        }
        return adicionado
    }

    suspend fun removeExercicio(dia: String, exercicio: String) {
        context.exerciciosDataStore.edit { prefs ->
            val chave = keyParaDia(dia)
            val listaAtual = prefs[chave]?.toMutableSet() ?: return@edit
            listaAtual.remove(exercicio)
            prefs[chave] = listaAtual
        }
    }
}
