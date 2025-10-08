// F_ARQUIVO: ui/screens/TelaPacientesVinculados.kt
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
import com.tazy.simplepillfinal.model.Paciente
import com.tazy.simplepillfinal.model.TipoUsuario
import com.tazy.simplepillfinal.navigation.AppRoutes
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaPacientesVinculados(
    navController: NavController,
    uid: String,
    tipo: TipoUsuario,
    viewModel: PacientesVinculadosViewModel = viewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.carregarPacientes(uid, tipo)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE5F4F5))
    ) {
        // Onda superior para consistência visual
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
                color = Color(0xFF73A5AD)
            )
        }

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Pacientes Vinculados", color = Color.White) },
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
                if (viewModel.isLoading) {
                    CircularProgressIndicator()
                } else if (viewModel.errorMessage != null) {
                    Text(text = "Erro: ${viewModel.errorMessage}")
                } else if (viewModel.pacientes.isEmpty()) {
                    Text(text = "Nenhum paciente vinculado encontrado.")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(viewModel.pacientes) { paciente ->
                            PacienteCard(
                                paciente = paciente,
                                onClick = {
                                    val encodedNome = URLEncoder.encode(paciente.nome, StandardCharsets.UTF_8.toString())
                                    if (tipo == TipoUsuario.PROFISSIONAL_SAUDE) {
                                        navController.navigate("${AppRoutes.ACOES_PACIENTE}/${paciente.uid}/$encodedNome")
                                    } else if (tipo == TipoUsuario.CUIDADOR) {
                                        navController.navigate("${AppRoutes.VISUALIZAR_DADOS_PACIENTE}/${paciente.uid}/$encodedNome")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PacienteCard(paciente: Paciente, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = paciente.nome, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "E-mail: ${paciente.email}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}