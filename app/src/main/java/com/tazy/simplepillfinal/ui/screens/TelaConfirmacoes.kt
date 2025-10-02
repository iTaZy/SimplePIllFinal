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
import com.tazy.simplepillfinal.model.SolicitacaoVinculacao

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaConfirmacoes(
    navController: NavController,
    pacienteUid: String,
    viewModel: ConfirmacoesViewModel = viewModel()
) {
    LaunchedEffect(key1 = pacienteUid) {
        viewModel.carregarSolicitacoes(pacienteUid)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmações de Vinculação") },
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
                viewModel.solicitacoes.isEmpty() -> Text(text = "Nenhuma solicitação pendente.")
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(viewModel.solicitacoes) { solicitacao ->
                            SolicitacaoVinculacaoCard(
                                solicitacao = solicitacao,
                                onAceitar = { viewModel.aceitarSolicitacao(pacienteUid, solicitacao.solicitanteUid, solicitacao.tipo) },
                                onNegar = { viewModel.negarSolicitacao(pacienteUid, solicitacao.solicitanteUid, solicitacao.tipo) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SolicitacaoVinculacaoCard(
    solicitacao: SolicitacaoVinculacao,
    onAceitar: () -> Unit,
    onNegar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Nova solicitação de vínculo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "O(a) ${solicitacao.tipo.name.lowercase()} ${solicitacao.solicitanteNome} deseja se vincular a você.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onNegar) {
                    Text("Negar", color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onAceitar) {
                    Text("Aceitar")
                }
            }
        }
    }
}