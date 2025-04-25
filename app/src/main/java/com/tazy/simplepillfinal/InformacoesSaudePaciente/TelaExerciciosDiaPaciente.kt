package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tazy.simplepillfinal.R
import com.tazy.simplepillfinal.ui.components.BotaoOval
import com.tazy.simplepillfinal.ui.viewmodel.ExerciciosDataStoreViewModel
import com.tazy.simplepillfinal.ui.viewmodel.ExerciciosDataStoreViewModelFactory

@Composable
fun TelaExerciciosDiaPaciente(
    navController: NavController,
    dia: String
) {
    val azul = Color(0xFF7AA7B4)
    val context = LocalContext.current
    val viewModel: ExerciciosDataStoreViewModel = viewModel(
        factory = ExerciciosDataStoreViewModelFactory(context)
    )

    // Carrega exercícios ao entrar
    LaunchedEffect(dia) { viewModel.load(dia) }
    val lista by viewModel.lista.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var novoExercicio by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Topo curvo branco
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            val w = size.width
            val h = size.height
            drawPath(
                path = Path().apply {
                    moveTo(0f, h * 0.7f)
                    cubicTo(w * 0.25f, h, w * 0.75f, h * 0.2f, w, h * 0.7f)
                    lineTo(w, 0f); lineTo(0f, 0f); close()
                },
                color = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(azul)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Exercícios de $dia",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(Modifier.height(24.dp))

            if (lista.isEmpty()) {
                Text(
                    text = "Nenhum exercício cadastrado",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                lista.forEach { exercicio ->
                    Surface(
                        tonalElevation = 4.dp,
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = exercicio,
                                fontSize = 18.sp,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(onClick = { viewModel.remove(dia, exercicio) }) {
                                Text(
                                    text = "Remover",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        textDecoration = TextDecoration.Underline
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            BotaoOval(texto = "Adicionar Exercício") { showDialog = true }
            Spacer(Modifier.weight(1f))

            Text(
                text = "Voltar",
                color = Color.White,
                fontSize = 16.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .padding(vertical = 8.dp)
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Adicionar Exercício") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = novoExercicio,
                            onValueChange = {
                                novoExercicio = it
                                showError = false
                            },
                            label = { Text("Nome do exercício") },
                            isError = showError,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (showError) {
                            Text(
                                text = "Exercício inválido ou duplicado!",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (novoExercicio.isNotBlank()) {
                            viewModel.add(dia, novoExercicio.trim()) { sucesso ->
                                if (sucesso) {
                                    showDialog = false
                                    novoExercicio = ""
                                } else showError = true
                            }
                        } else showError = true
                    }) {
                        Text("Adicionar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        novoExercicio = ""
                        showDialog = false
                    }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
