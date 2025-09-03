// CRIE ESTE NOVO ARQUIVO: ui/screens/TelaPrescreverMedicacao.kt
package com.tazy.simplepillfinal.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaPrescreverMedicacao(
    navController: NavController,
    pacienteUid: String,
    pacienteNome: String,
    viewModel: PrescreverMedicacaoViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(viewModel.prescriptionSuccess) {
        if (viewModel.prescriptionSuccess) {
            Toast.makeText(context, "Medicação prescrita com sucesso!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            Toast.makeText(context, "Erro: $it", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Prescrever Medicação") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Paciente: $pacienteNome", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(value = viewModel.nome, onValueChange = { viewModel.nome = it }, label = { Text("Nome da Medicação*") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = viewModel.dosagem, onValueChange = { viewModel.dosagem = it }, label = { Text("Dosagem (ex: 500mg, 1 comprimido)") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = viewModel.frequencia, onValueChange = { viewModel.frequencia = it }, label = { Text("Frequência (ex: a cada 8 horas)") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = viewModel.duracao, onValueChange = { viewModel.duracao = it }, label = { Text("Duração (ex: por 7 dias)") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = viewModel.observacoes, onValueChange = { viewModel.observacoes = it }, label = { Text("Observações") }, modifier = Modifier.fillMaxWidth(), maxLines = 4)
            Spacer(Modifier.height(32.dp))

            Button(
                onClick = { viewModel.onPrescribeClick(pacienteUid) },
                enabled = !viewModel.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(if (viewModel.isLoading) "Salvando..." else "Salvar Prescrição")
            }
        }
    }
}