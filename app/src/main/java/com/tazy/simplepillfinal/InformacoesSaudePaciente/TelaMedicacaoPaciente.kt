package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tazy.simplepillfinal.R
import com.tazy.simplepillfinal.ui.components.BotaoOval
import com.tazy.simplepillfinal.ui.viewmodel.MedicacaoPacienteViewModel
import com.tazy.simplepillfinal.ui.viewmodel.MedicacaoPacienteViewModelFactory
import com.tazy.simplepillfinal.data.model.Medicacao

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaMedicacaoPaciente(
    onVoltar: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: MedicacaoPacienteViewModel = viewModel(
        factory = MedicacaoPacienteViewModelFactory(context)
    )

    var showDialog by remember { mutableStateOf(false) }
    var nome by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }
    var comRefeicao by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    val lista by viewModel.medicacoes.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7AA7B4)) // Fundo azul
    ) {
        // Curva branca no topo
        Canvas(
            Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            // TODO: desenhar curva branca aqui
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp)) // Espaço para a curva

            Text(
                text = "Suas Medicações",
                fontSize = 32.sp,
                color = Color.Black
            )
            Spacer(Modifier.height(16.dp))

            if (lista.isEmpty()) {
                Text(
                    text = "Nenhuma medicação cadastrada",
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(lista) { m: Medicacao ->
                        // Caixa minimalista
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .border(
                                    width = 1.dp,
                                    color = Color.Gray,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = m.nome,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = "Horário: ${m.horario}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = if (m.comRefeicao) "Com refeição" else "Sem refeição",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    text = "Remover",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        textDecoration = TextDecoration.Underline
                                    ),
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .clickable { viewModel.removeMedicacao(m.nome) }
                                        .padding(4.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            BotaoOval(texto = "Adicionar Medicação") {
                nome = ""
                horario = ""
                comRefeicao = false
                showError = false
                showDialog = true
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Voltar",
                fontSize = 18.sp,
                textDecoration = TextDecoration.Underline,
                color = Color.Black,
                modifier = Modifier
                    .clickable { onVoltar() }
                    .padding(8.dp)
            )

            Spacer(Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.logo_simple_pill),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Adicionar Medicação") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = nome,
                            onValueChange = { nome = it; showError = false },
                            label = { Text("Nome") },
                            isError = showError,
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                cursorColor = Color.Black,
                                focusedBorderColor = Color.Black,
                                unfocusedBorderColor = Color.Gray
                            )
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = horario,
                            onValueChange = { horario = it },
                            label = { Text("Horário (ex: 08:00)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                cursorColor = Color.Black,
                                focusedBorderColor = Color.Black,
                                unfocusedBorderColor = Color.Gray
                            )
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = comRefeicao,
                                onCheckedChange = { comRefeicao = it },
                                colors = CheckboxDefaults.colors(checkedColor = Color.Black)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("Tomar com refeição", color = Color.Black)
                        }
                        if (showError) {
                            Text(
                                text = "Nome já existe ou está vazio!",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (nome.isNotBlank() && horario.isNotBlank()) {
                            viewModel.addMedicacao(
                                Medicacao(nome, horario, comRefeicao)
                            ) { ok ->
                                if (ok) showDialog = false else showError = true
                            }
                        } else showError = true
                    }) {
                        Text("Salvar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
