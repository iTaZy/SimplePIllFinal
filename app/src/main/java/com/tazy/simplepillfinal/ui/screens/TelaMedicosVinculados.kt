// F_ARQUIVO: ui/screens/TelaMedicosVinculados.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tazy.simplepillfinal.model.Usuario
import com.tazy.simplepillfinal.navigation.AppRoutes
import com.tazy.simplepillfinal.ui.components.UsuarioListItem
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaMedicosVinculados(
    navController: NavController,
    pacienteUid: String,
    viewModel: MedicosVinculadosViewModel = viewModel()
) {
    LaunchedEffect(key1 = pacienteUid) {
        viewModel.carregarMedicosVinculados(pacienteUid)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Médicos Vinculados") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                viewModel.isLoading -> CircularProgressIndicator()
                viewModel.errorMessage != null -> Text(text = "Erro: ${viewModel.errorMessage}")
                viewModel.medicos.isEmpty() -> Text(text = "Nenhum médico vinculado.")
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(viewModel.medicos) { medico ->
                            UsuarioListItem(
                                nome = medico.nome,
                                email = medico.email,
                                tipo = "Médico",
                                onClick = {
                                    val encodedMedicoNome = URLEncoder.encode(medico.nome, StandardCharsets.UTF_8.toString())
                                    navController.navigate("${AppRoutes.DETALHES_MEDICO}/$pacienteUid/${medico.id}/$encodedMedicoNome")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}