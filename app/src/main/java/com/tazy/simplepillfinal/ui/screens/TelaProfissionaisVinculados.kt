// F_ARQUIVO: ui/screens/TelaProfissionaisVinculados.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
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

    val headerColor = Color(0xFF74ABBF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5)),
        contentAlignment = Alignment.Center
    ) {
        // Onda superior
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .align(Alignment.TopCenter)
        ) {
            val w = size.width
            val h = size.height
            drawPath(
                path = Path().apply {
                    moveTo(0f, h * 0.6f)
                    cubicTo(w * 0.25f, h * 1.2f, w * 0.75f, h * 0.5f, w, h * 0.8f)
                    lineTo(w, 0f)
                    lineTo(0f, 0f)
                    close()
                },
                color = headerColor
            )
        }

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Profissionais e Cuidadores", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
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
                    viewModel.vinculados.isEmpty() -> Text(text = "Nenhum profissional ou cuidador vinculado encontrado.", color = Color.Gray)
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
}