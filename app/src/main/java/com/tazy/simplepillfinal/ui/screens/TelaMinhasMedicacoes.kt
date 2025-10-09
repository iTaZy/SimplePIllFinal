// F_ARQUIVO: ui/screens/TelaMinhasMedicacoes.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tazy.simplepillfinal.model.Medicacao
import com.tazy.simplepillfinal.navigation.AppRoutes
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaMinhasMedicacoes(
    navController: NavController,
    pacienteUid: String,
    viewModel: MinhasMedicacoesViewModel = viewModel()
) {
    LaunchedEffect(key1 = pacienteUid) {
        viewModel.carregarMedicacoes(pacienteUid)
    }

    val headerColor = Color(0xFF74ABBF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
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
                    title = { Text("Minhas Medicações", color = Color.White) },
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
                    viewModel.medicacoes.isEmpty() -> Text(text = "Nenhuma medicação encontrada.")
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(viewModel.medicacoes) { medicacao ->
                                MedicacaoCard(medicacao) {
                                    val tipoAcao = URLEncoder.encode("Medicações", StandardCharsets.UTF_8.toString())
                                    navController.navigate("${AppRoutes.DETALHES_REGISTRO}/$tipoAcao/${medicacao.id}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MedicacaoCard(medicacao: Medicacao, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val dataFormatada = medicacao.dataPrescricao?.toDate()?.let {
                java.text.SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", java.util.Locale.getDefault()).format(it)
            } ?: "Data indisponível"

            Text(
                text = medicacao.nome,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow("Dosagem:", medicacao.dosagem)
            InfoRow("Frequência:", medicacao.frequencia)
            InfoRow("Duração:", medicacao.duracao)
            if (medicacao.observacoes.isNotBlank()) {
                InfoRow("Observações:", medicacao.observacoes)
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = "Prescrito em: $dataFormatada",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    if (value.isNotBlank()) {
        Row {
            Text(
                text = label,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.width(120.dp)
            )
            Text(text = value)
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}