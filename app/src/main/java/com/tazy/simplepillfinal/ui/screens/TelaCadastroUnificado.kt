// F_ARQUIVO: ui/screens/TelaCadastroUnificado.kt
package com.tazy.simplepillfinal.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
    acao: String,
    viewModel: CadastroUnificadoViewModel = viewModel()
) {
    val context = LocalContext.current
    val decodedPacienteNome = URLDecoder.decode(pacienteNome, StandardCharsets.UTF_8.toString())
    val decodedAcao = URLDecoder.decode(acao, StandardCharsets.UTF_8.toString())

    val backgroundColor = Color(0xFFE2C64D)

    LaunchedEffect(viewModel.saveSuccess) {
        if (viewModel.saveSuccess) {
            Toast.makeText(context, "Registro salvo com sucesso!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            Toast.makeText(context, "Erro: $it", Toast.LENGTH_LONG).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                Image(
                    painter = painterResource(id = R.drawable.ic_person_placeholder),
                    contentDescription = "Foto do Paciente",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
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
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit_pencil),
                            contentDescription = "Editar",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(Modifier.height(32.dp))

                    when (decodedAcao) {
                        "Exames" -> SubtelaExames(viewModel)
                        "Vacinação" -> SubtelaVacinacao(viewModel)
                        "Internação" -> SubtelaInternacao(viewModel)
                        "Fisioterapia" -> SubtelaFisioterapia(viewModel)
                        "Saúde mental" -> SubtelaSaudeMental(viewModel)
                        "Nutrição" -> SubtelaNutricao(viewModel)
                        else -> Text("Ação não reconhecida.", color = Color.Gray)
                    }

                    Spacer(Modifier.weight(1f))

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
                            onClick = { viewModel.onSaveClick(pacienteUid, decodedAcao) },
                            enabled = !viewModel.isLoading,
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = backgroundColor,
                                contentColor = Color.Black
                            ),
                            modifier = Modifier
                                .width(120.dp)
                                .height(50.dp)
                        ) {
                            Text(
                                if (viewModel.isLoading) "Salvando..." else "Salvar",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

// As subtelas agora recebem o ViewModel como parâmetro
@Composable
private fun SubtelaExames(viewModel: CadastroUnificadoViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = viewModel.examePedido,
            onValueChange = { viewModel.examePedido = it },
            label = { Text("Exame pedido") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.unidadeExame,
            onValueChange = { viewModel.unidadeExame = it },
            label = { Text("Unidade") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.diagnosticoExame,
            onValueChange = { viewModel.diagnosticoExame = it },
            label = { Text("Suspeita de diagnóstico") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
    }
}

@Composable
private fun SubtelaVacinacao(viewModel: CadastroUnificadoViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = viewModel.vacina1,
            onValueChange = { viewModel.vacina1 = it },
            label = { Text("Prescrever vacina 1") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.vacina2,
            onValueChange = { viewModel.vacina2 = it },
            label = { Text("Prescrever vacina 2") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.vacina3,
            onValueChange = { viewModel.vacina3 = it },
            label = { Text("Prescrever vacina 3") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.vacina4,
            onValueChange = { viewModel.vacina4 = it },
            label = { Text("Prescrever vacina 4") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
    }
}

@Composable
private fun SubtelaInternacao(viewModel: CadastroUnificadoViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = viewModel.unidadeInternacao,
            onValueChange = { viewModel.unidadeInternacao = it },
            label = { Text("Unidade") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.motivoInternacao,
            onValueChange = { viewModel.motivoInternacao = it },
            label = { Text("Motivo") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.dataInternacao,
            onValueChange = { viewModel.dataInternacao = it },
            label = { Text("Data") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
    }
}

@Composable
private fun SubtelaFisioterapia(viewModel: CadastroUnificadoViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = viewModel.dataFisioterapia,
            onValueChange = { viewModel.dataFisioterapia = it },
            label = { Text("Data") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.localFisioterapia,
            onValueChange = { viewModel.localFisioterapia = it },
            label = { Text("Local") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.sessoesFisioterapia,
            onValueChange = { viewModel.sessoesFisioterapia = it },
            label = { Text("Número de sessões") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.diagnosticoFisioterapia,
            onValueChange = { viewModel.diagnosticoFisioterapia = it },
            label = { Text("Diagnóstico") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
    }
}

@Composable
private fun SubtelaSaudeMental(viewModel: CadastroUnificadoViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = viewModel.unidadeSaudeMental,
            onValueChange = { viewModel.unidadeSaudeMental = it },
            label = { Text("Unidade") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.tratamentoSaudeMental,
            onValueChange = { viewModel.tratamentoSaudeMental = it },
            label = { Text("Tipo de tratamento") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.dataSaudeMental,
            onValueChange = { viewModel.dataSaudeMental = it },
            label = { Text("Data") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.duracaoSaudeMental,
            onValueChange = { viewModel.duracaoSaudeMental = it },
            label = { Text("Duração") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
    }
}

@Composable
private fun SubtelaNutricao(viewModel: CadastroUnificadoViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = viewModel.dataNutricao,
            onValueChange = { viewModel.dataNutricao = it },
            label = { Text("Data") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.localNutricao,
            onValueChange = { viewModel.localNutricao = it },
            label = { Text("Local") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.diagnosticoNutricao,
            onValueChange = { viewModel.diagnosticoNutricao = it },
            label = { Text("Diagnóstico") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Gray)
        )
    }
}