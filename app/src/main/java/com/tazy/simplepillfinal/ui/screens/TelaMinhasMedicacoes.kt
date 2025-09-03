// CRIE ESTE NOVO ARQUIVO: ui/screens/TelaMinhasMedicacoes.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tazy.simplepillfinal.model.Medicacao
import java.text.SimpleDateFormat
import java.util.*

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas Medicações") },
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
                viewModel.medicacoes.isEmpty() -> Text(text = "Nenhuma medicação encontrada.")
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(viewModel.medicacoes) { medicacao ->
                            MedicacaoCard(medicacao)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MedicacaoCard(medicacao: Medicacao) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val sdf = SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", Locale.getDefault())
            val dataFormatada = sdf.format(medicacao.dataPrescricao.toDate())

            Text(text = medicacao.nome, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
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
                style = MaterialTheme.typography.bodySmall
            )
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