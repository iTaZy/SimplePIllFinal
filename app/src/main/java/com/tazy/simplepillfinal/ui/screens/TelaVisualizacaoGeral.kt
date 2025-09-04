// F_ARQUIVO: ui/screens/TelaVisualizacaoGeral.kt
package com.tazy.simplepillfinal.ui.screens

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
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import com.tazy.simplepillfinal.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisualizacaoGeralScreen(
    navController: NavController,
    pacienteUid: String,
    acao: String,
    viewModel: VisualizacaoGeralViewModel = viewModel()
) {
    // Carrega os dados quando a tela é iniciada
    LaunchedEffect(key1 = acao, key2 = pacienteUid) {
        viewModel.carregarDados(pacienteUid, acao)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = acao) },
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
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(dataList) { item ->
                                // Composable Card para cada item da lista
                                when (item) {
                                    is Exame -> ExameCard(exame = item)
                                    is Vacinacao -> VacinacaoCard(vacinacao = item)
                                    is Internacao -> InternacaoCard(internacao = item)
                                    is Fisioterapia -> FisioterapiaCard(fisioterapia = item)
                                    is SaudeMental -> SaudeMentalCard(saudeMental = item)
                                    is Nutricao -> NutricaoCard(nutricao = item)
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
fun ExameCard(exame: Exame) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Exame: ${exame.examePedido}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow("Unidade:", exame.unidade)
            InfoRow("Diagnóstico:", exame.diagnostico)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(text = "Solicitado em: ${formatTimestamp(exame.dataSolicitacao)}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun VacinacaoCard(vacinacao: Vacinacao) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Vacinação", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow("Vacina 1:", vacinacao.vacina1)
            InfoRow("Vacina 2:", vacinacao.vacina2)
            InfoRow("Vacina 3:", vacinacao.vacina3)
            InfoRow("Vacina 4:", vacinacao.vacina4)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(text = "Registrado em: ${formatTimestamp(vacinacao.dataSolicitacao)}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun InternacaoCard(internacao: Internacao) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Internação", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow("Unidade:", internacao.unidade)
            InfoRow("Motivo:", internacao.motivo)
            InfoRow("Data:", internacao.data)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(text = "Registrado em: ${formatTimestamp(internacao.dataRegistro)}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun FisioterapiaCard(fisioterapia: Fisioterapia) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Fisioterapia", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow("Data:", fisioterapia.data)
            InfoRow("Local:", fisioterapia.local)
            InfoRow("Sessões:", fisioterapia.sessoes)
            InfoRow("Diagnóstico:", fisioterapia.diagnostico)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(text = "Registrado em: ${formatTimestamp(fisioterapia.dataRegistro)}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun SaudeMentalCard(saudeMental: SaudeMental) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Saúde Mental", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow("Unidade:", saudeMental.unidade)
            InfoRow("Tratamento:", saudeMental.tratamento)
            InfoRow("Data:", saudeMental.data)
            InfoRow("Duração:", saudeMental.duracao)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(text = "Registrado em: ${formatTimestamp(saudeMental.dataRegistro)}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun NutricaoCard(nutricao: Nutricao) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nutrição", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow("Data:", nutricao.data)
            InfoRow("Local:", nutricao.local)
            InfoRow("Diagnóstico:", nutricao.diagnostico)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(text = "Registrado em: ${formatTimestamp(nutricao.dataRegistro)}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    if (value.isNotBlank()) {
        Row {
            Text(text = label, fontWeight = FontWeight.SemiBold, modifier = Modifier.width(120.dp))
            Text(text = value)
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

fun formatTimestamp(timestamp: Timestamp): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", Locale.getDefault())
    return sdf.format(timestamp.toDate())
}