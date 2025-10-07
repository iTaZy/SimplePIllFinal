// F_ARQUIVO: ui/screens/TelaProfissionaisVinculados.kt
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tazy.simplepillfinal.model.TipoUsuario
import com.tazy.simplepillfinal.model.Usuario
import com.tazy.simplepillfinal.navigation.AppRoutes
import com.tazy.simplepillfinal.ui.components.UsuarioListItem
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaProfissionaisVinculados(
    navController: NavController,
    pacienteUid: String,
    viewModel: ProfissionaisVinculadosViewModel = viewModel()
) {
    LaunchedEffect(key1 = pacienteUid) {
        viewModel.carregarVinculados(pacienteUid)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profissionais e Cuidadores") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
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
                viewModel.vinculados.isEmpty() -> Text(text = "Nenhum profissional ou cuidador vinculado encontrado.")
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(viewModel.vinculados) { usuario ->
                            UsuarioListItem(
                                nome = usuario.nome,
                                email = usuario.email,
                                tipo = when (usuario.tipo) {
                                    TipoUsuario.PROFISSIONAL_SAUDE -> "Profissional de Saúde"
                                    TipoUsuario.CUIDADOR -> "Cuidador"
                                    else -> "Desconhecido"
                                },
                                onClick = {
                                    val encodedNome = URLEncoder.encode(usuario.nome, StandardCharsets.UTF_8.toString())
                                    navController.navigate("${AppRoutes.DETALHES_MEDICO}/$pacienteUid/${usuario.uid}/$encodedNome")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}