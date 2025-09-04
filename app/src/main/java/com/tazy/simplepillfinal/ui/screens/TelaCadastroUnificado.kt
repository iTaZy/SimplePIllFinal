// F_ARQUIVO: ui/screens/TelaCadastroUnificado.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tazy.simplepillfinal.R
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastroUnificado(
    navController: NavController,
    pacienteUid: String,
    pacienteNome: String,
    acao: String
) {
    val decodedPacienteNome = URLDecoder.decode(pacienteNome, StandardCharsets.UTF_8.toString())
    val decodedAcao = URLDecoder.decode(acao, StandardCharsets.UTF_8.toString())

    val backgroundColor = Color(0xFFE2C64D) // Amarelo do design

    // Usamos um Box para sobrepor o conteúdo sobre o fundo amarelo
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Cabeçalho similar ao da TelaAcoesPaciente
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Paciente,\n$decodedPacienteNome",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    lineHeight = 36.sp
                )
                // Usando o icone de pessoa, você pode substituir por um icone de caneta
                Image(
                    painter = painterResource(id = R.drawable.ic_person_placeholder), // Assumindo que você tem esse recurso
                    contentDescription = "Foto do Paciente",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Cartão branco para o conteúdo principal
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Preenche o espaço restante
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = decodedAcao,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                        // Ícone de lápis/caneta ao lado do título (similar ao design)
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit_pencil), // Você precisará deste recurso
                            contentDescription = "Editar",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(Modifier.height(32.dp))

                    // Conteúdo dinâmico da subtela
                    when (decodedAcao) {
                        "Exames" -> {
                            SubtelaExames()
                        }
                        "Vacinação" -> {
                            SubtelaVacinacao()
                        }
                        "Internação" -> {
                            SubtelaInternacao()
                        }
                        "Fisioterapia" -> {
                            SubtelaFisioterapia()
                        }
                        "Saúde mental" -> {
                            SubtelaSaudeMental()
                        }
                        "Nutrição" -> {
                            SubtelaNutricao()
                        }
                        else -> {
                            Text("Ação não reconhecida.", color = Color.Gray)
                        }
                    }

                    Spacer(Modifier.weight(1f)) // Empurra os botões para baixo

                    // Botões "Voltar" e "Salvar" na parte inferior do cartão branco
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Voltar",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.clickable { navController.popBackStack() }
                        )
                        Button(
                            onClick = {
                                // TODO: Implementar lógica de salvar usando o pacienteUid
                                // Por exemplo: viewModel.salvarExame(pacienteUid, ...)
                            },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = backgroundColor, // O botão "Salvar" pode usar o amarelo
                                contentColor = Color.Black
                            ),
                            modifier = Modifier
                                .width(120.dp)
                                .height(50.dp)
                        ) {
                            Text("Salvar", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}

// As subtelas (SubtelaExames, SubtelaVacinacao, etc.) agora só precisam se preocupar com seus próprios campos.
// Elas não precisam de Scaffold, TopAppBar ou botões de navegação, pois o TelaCadastroUnificado já os fornece.

@Composable
private fun SubtelaExames() {
    var exame by remember { mutableStateOf("") }
    var unidade by remember { mutableStateOf("") }
    var diagnostico by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = exame,
            onValueChange = { exame = it },
            label = { Text("Exame pedido") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = unidade,
            onValueChange = { unidade = it },
            label = { Text("Unidade") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = diagnostico,
            onValueChange = { diagnostico = it },
            label = { Text("Suspeita de diagnóstico") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
    }
}

@Composable
private fun SubtelaVacinacao() {
    var vacina1 by remember { mutableStateOf("") }
    var vacina2 by remember { mutableStateOf("") }
    var vacina3 by remember { mutableStateOf("") }
    var vacina4 by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = vacina1,
            onValueChange = { vacina1 = it },
            label = { Text("Prescrever vacina 1") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = vacina2,
            onValueChange = { vacina2 = it },
            label = { Text("Prescrever vacina 2") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = vacina3,
            onValueChange = { vacina3 = it },
            label = { Text("Prescrever vacina 3") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = vacina4,
            onValueChange = { vacina4 = it },
            label = { Text("Prescrever vacina 4") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
    }
}

@Composable
private fun SubtelaInternacao() {
    var unidade by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = unidade,
            onValueChange = { unidade = it },
            label = { Text("Unidade") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = motivo,
            onValueChange = { motivo = it },
            label = { Text("Motivo") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = data,
            onValueChange = { data = it },
            label = { Text("Data") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
    }
}

@Composable
private fun SubtelaFisioterapia() {
    var data by remember { mutableStateOf("") }
    var local by remember { mutableStateOf("") }
    var sessoes by remember { mutableStateOf("") }
    var diagnostico by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = data,
            onValueChange = { data = it },
            label = { Text("Data") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = local,
            onValueChange = { local = it },
            label = { Text("Local") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = sessoes,
            onValueChange = { sessoes = it },
            label = { Text("Número de sessões") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = diagnostico,
            onValueChange = { diagnostico = it },
            label = { Text("Diagnóstico") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
    }
}

@Composable
private fun SubtelaSaudeMental() {
    var unidade by remember { mutableStateOf("") }
    var tratamento by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }
    var duracao by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = unidade,
            onValueChange = { unidade = it },
            label = { Text("Unidade") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = tratamento,
            onValueChange = { tratamento = it },
            label = { Text("Tipo de tratamento") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = data,
            onValueChange = { data = it },
            label = { Text("Data") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = duracao,
            onValueChange = { duracao = it },
            label = { Text("Duração") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
    }
}

@Composable
private fun SubtelaNutricao() {
    var data by remember { mutableStateOf("") }
    var local by remember { mutableStateOf("") }
    var diagnostico by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = data,
            onValueChange = { data = it },
            label = { Text("Data") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = local,
            onValueChange = { local = it },
            label = { Text("Local") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = diagnostico,
            onValueChange = { diagnostico = it },
            label = { Text("Diagnóstico") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray
            )
        )
    }
}