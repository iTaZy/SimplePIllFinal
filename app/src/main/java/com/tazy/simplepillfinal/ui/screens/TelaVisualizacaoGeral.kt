// F_ARQUIVO: ui/screens/TelaVisualizacaoGeral.kt
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.tazy.simplepillfinal.model.*
import com.tazy.simplepillfinal.navigation.AppRoutes
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisualizacaoGeralScreen(
    navController: NavController,
    pacienteUid: String,
    acao: String,
    viewModel: VisualizacaoGeralViewModel = viewModel()
) {
    LaunchedEffect(key1 = acao, key2 = pacienteUid) {
        viewModel.carregarDados(pacienteUid, acao)
    }

    // Cor de fundo do cabeçalho
    val headerColor = Color(0xFF74ABBF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5)) // Fundo cinza claro
    ) {
        // Onda superior
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .align(Alignment.TopCenter)
        ) {
            val w = size.width
            val h = size.height
            drawPath(
                path = Path().apply {
                    moveTo(0f, h * 0.8f)
                    cubicTo(w * 0.25f, h * 1.2f, w * 0.75f, h * 0.5f, w, h * 0.8f)
                    lineTo(w, 0f)
                    lineTo(0f, 0f)
                    close()
                },
                color = headerColor
            )
        }

        // Conteúdo da tela dentro de um Scaffold
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(text = acao, color = Color.White) },
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
                    else -> {
                        val dataList: List<Any> = when (acao) {
                            "Exames" -> viewModel.exames
                            "Vacinação" -> viewModel.vacinacao
                            "Internações" -> viewModel.internacoes
                            "Fisioterapia" -> viewModel.fisioterapia
                            "Saúde Mental" -> viewModel.saudeMental
                            "Nutrição" -> viewModel.nutricao
                            else -> emptyList()
                        }
                        if (dataList.isEmpty()) {
                            Text(text = "Nenhum registro de $acao encontrado.")
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 16.dp), // Espaço extra para o cabeçalho
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(dataList) { item ->
                                    val encodedAcao = URLEncoder.encode(acao, StandardCharsets.UTF_8.toString())
                                    when (item) {
                                        is Exame -> ExameCard(exame = item) {
                                            navController.navigate("${AppRoutes.DETALHES_REGISTRO}/$encodedAcao/${item.id}")
                                        }
                                        is Vacinacao -> VacinacaoCard(vacinacao = item) {
                                            navController.navigate("${AppRoutes.DETALHES_REGISTRO}/$encodedAcao/${item.id}")
                                        }
                                        is Internacao -> InternacaoCard(internacao = item) {
                                            navController.navigate("${AppRoutes.DETALHES_REGISTRO}/$encodedAcao/${item.id}")
                                        }
                                        is Fisioterapia -> FisioterapiaCard(fisioterapia = item) {
                                            navController.navigate("${AppRoutes.DETALHES_REGISTRO}/$encodedAcao/${item.id}")
                                        }
                                        is SaudeMental -> SaudeMentalCard(saudeMental = item) {
                                            navController.navigate("${AppRoutes.DETALHES_REGISTRO}/$encodedAcao/${item.id}")
                                        }
                                        is Nutricao -> NutricaoCard(nutricao = item) {
                                            navController.navigate("${AppRoutes.DETALHES_REGISTRO}/$encodedAcao/${item.id}")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Cards para cada tipo de registro
@Composable
fun ExameCard(exame: Exame, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Exame: ${exame.examePedido}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow("Unidade:", exame.unidade)
            InfoRow("Diagnóstico:", exame.diagnostico)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = "Solicitado em: ${formatTimestamp(exame.dataSolicitacao)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun VacinacaoCard(vacinacao: Vacinacao, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Vacinação",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow("Vacina 1:", vacinacao.vacina1)
            InfoRow("Vacina 2:", vacinacao.vacina2)
            InfoRow("Vacina 3:", vacinacao.vacina3)
            InfoRow("Vacina 4:", vacinacao.vacina4)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = "Registrado em: ${formatTimestamp(vacinacao.dataSolicitacao)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun InternacaoCard(internacao: Internacao, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Internação",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow("Unidade:", internacao.unidade)
            InfoRow("Motivo:", internacao.motivo)
            InfoRow("Data:", internacao.data)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = "Registrado em: ${formatTimestamp(internacao.dataRegistro)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun FisioterapiaCard(fisioterapia: Fisioterapia, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Fisioterapia",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow("Data:", fisioterapia.data)
            InfoRow("Local:", fisioterapia.local)
            InfoRow("Sessões:", fisioterapia.sessoes)
            InfoRow("Diagnóstico:", fisioterapia.diagnostico)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = "Registrado em: ${formatTimestamp(fisioterapia.dataRegistro)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun SaudeMentalCard(saudeMental: SaudeMental, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Saúde Mental",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow("Unidade:", saudeMental.unidade)
            InfoRow("Tratamento:", saudeMental.tratamento)
            InfoRow("Data:", saudeMental.data)
            InfoRow("Duração:", saudeMental.duracao)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = "Registrado em: ${formatTimestamp(saudeMental.dataRegistro)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun NutricaoCard(nutricao: Nutricao, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Nutrição",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow("Data:", nutricao.data)
            InfoRow("Local:", nutricao.local)
            InfoRow("Diagnóstico:", nutricao.diagnostico)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = "Registrado em: ${formatTimestamp(nutricao.dataRegistro)}",
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
                modifier = Modifier.width(120.dp),
                color = Color.Black.copy(alpha = 0.8f)
            )
            Text(
                text = value,
                color = Color.Black.copy(alpha = 0.6f)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}