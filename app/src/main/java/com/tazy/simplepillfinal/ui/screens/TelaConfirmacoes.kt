// F_ARQUIVO: ui/screens/TelaConfirmacoes.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tazy.simplepillfinal.model.SolicitacaoVinculo

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

    val headerColor = Color(0xFF74ABBF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5)),
        contentAlignment = Alignment.Center
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
                color = headerColor
            )
        }

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Confirmações de Vínculo", color = Color.White) },
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
                    viewModel.solicitacoes.isEmpty() -> Text(text = "Nenhuma solicitação pendente.", color = Color.Gray)
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(viewModel.solicitacoes) { solicitacao ->
                                SolicitacaoVinculoCard(
                                    solicitacao = solicitacao,
                                    onAceitar = { viewModel.aceitarVinculacao(solicitacao) },
                                    onNegar = { viewModel.negarVinculacao(solicitacao) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SolicitacaoVinculoCard(
    solicitacao: SolicitacaoVinculo,
    onAceitar: () -> Unit,
    onNegar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Nova solicitação de vínculo",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tipo: ${solicitacao.remetenteTipo.replace("_", " ").lowercase().replaceFirstChar { it.titlecase() }}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "ID: ${solicitacao.remetenteUid}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = onAceitar,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E8B57))
                ) {
                    Text("Aceitar", color = Color.White)
                }
                OutlinedButton(
                    onClick = onNegar,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                ) {
                    Text("Negar")
                }
            }
        }
    }
}