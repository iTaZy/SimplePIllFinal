// F_ARQUIVO: ui/screens/TelaDetalhesMedico.kt
package com.tazy.simplepillfinal.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tazy.simplepillfinal.model.Medicacao
import com.tazy.simplepillfinal.model.TipoUsuario
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import androidx.compose.material.icons.filled.Person // Adicionado o import do ícone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalhesMedico(
    navController: NavController,
    pacienteUid: String,
    medicoId: String,
    medicoNome: String,
    viewModel: DetalhesProfissionalViewModel = viewModel()
) {
    val context = LocalContext.current
    val decodedMedicoNome = remember(medicoNome) {
        URLDecoder.decode(medicoNome, StandardCharsets.UTF_8.toString())
    }

    LaunchedEffect(key1 = medicoId) {
        // Corrigido para carregar tanto os detalhes quanto o histórico
        viewModel.carregarDetalhesEHistorico(pacienteUid, medicoId, TipoUsuario.PROFISSIONAL_SAUDE)
    }

    // Efeito para tratar erros
    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            Toast.makeText(context, "Erro: $it", Toast.LENGTH_LONG).show()
        }
    }

    // Efeito para navegar de volta após desvincular
    LaunchedEffect(viewModel.unlinkSuccess) {
        if (viewModel.unlinkSuccess) {
            Toast.makeText(context, "Profissional desvinculado.", Toast.LENGTH_SHORT).show()
            viewModel.clearState()
            navController.popBackStack()
        }
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
                .height(220.dp)
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

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Detalhes do Profissional", color = Color.White) },
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                when {
                    viewModel.isLoading && viewModel.profissional == null -> CircularProgressIndicator()
                    viewModel.errorMessage != null && viewModel.profissional == null -> Text(text = "Erro: ${viewModel.errorMessage}")
                    viewModel.profissional != null -> {
                        val profissional = viewModel.profissional!!

                        // Conteúdo da tela (Perfil e Histórico)
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Seção do perfil
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Person,
                                        contentDescription = "Ícone do Profissional",
                                        modifier = Modifier.size(80.dp),
                                        tint = Color.Gray // Cor do ícone
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = profissional.nome,
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "E-mail: ${profissional.email}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Função: ${
                                            when (profissional.tipo) {
                                                TipoUsuario.CUIDADOR -> "Cuidador"
                                                TipoUsuario.PROFISSIONAL_SAUDE -> "Profissional de Saúde"
                                                else -> "Desconhecido"
                                            }
                                        }",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    // Apenas exibe o CRM se o tipo for profissional de saúde
                                    if (profissional.tipo == TipoUsuario.PROFISSIONAL_SAUDE) {
                                        Text(
                                            text = "CRM: ${
                                                URLDecoder.decode(
                                                    medicoNome,
                                                    StandardCharsets.UTF_8.toString()
                                                )
                                            }",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Seção do histórico
                            Text(
                                text = "Histórico de Prescrições",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.Start)
                            )

                            if (viewModel.isLoading) {
                                CircularProgressIndicator()
                            } else if (viewModel.historicoMedicacoes.isEmpty()) {
                                Text(text = "Nenhuma medicação prescrita por este profissional.", color = Color.Gray)
                            } else {
                                // Usando Column em vez de LazyColumn para rolar junto com a tela
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    viewModel.historicoMedicacoes.forEach { medicacao ->
                                        DetalhesMedicoMedicacaoCard(medicacao)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // --- BOTÃO DE DESVINCULAR ADICIONADO ---
                            OutlinedButton(
                                onClick = { viewModel.togglePasswordDialog(true) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color.Red
                                ),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(Color.Red)
                                )
                            ) {
                                Text(if (viewModel.isLoading) "Aguarde..." else "Desvincular Profissional")
                            }
                        }
                    }
                    else -> {
                        Text(text = "Profissional não encontrado.")
                    }
                }
            }
        }

        // --- DIALOG DE CONFIRMAÇÃO DE SENHA ADICIONADO ---
        if (viewModel.showPasswordDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.togglePasswordDialog(false) },
                title = { Text("Confirme sua senha") },
                text = {
                    Column {
                        Text("Para desvincular este profissional, por favor, insira sua senha atual.")
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = viewModel.currentPassword,
                            onValueChange = { viewModel.currentPassword = it },
                            label = { Text("Senha atual") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (viewModel.errorMessage != null) {
                            Text(
                                text = viewModel.errorMessage!!,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.desfazerVinculo(pacienteUid, medicoId, viewModel.currentPassword)
                        },
                        enabled = viewModel.currentPassword.isNotBlank() && !viewModel.isLoading
                    ) {
                        Text(if (viewModel.isLoading) "Verificando..." else "Confirmar")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { viewModel.togglePasswordDialog(false) }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}


// Renomeado para evitar conflito
@Composable
fun DetalhesMedicoMedicacaoCard(medicacao: Medicacao) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                text = "Prescrito em: ${
                    medicacao.dataPrescricao?.toDate()?.let {
                        java.text.SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", java.util.Locale.getDefault()).format(it)
                    } ?: "Data indisponível"
                }",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

// Função auxiliar para exibir as informações
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