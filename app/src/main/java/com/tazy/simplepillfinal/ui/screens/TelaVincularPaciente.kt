// F_ARQUIVO: ui/screens/TelaVincularPaciente.kt
package com.tazy.simplepillfinal.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tazy.simplepillfinal.model.TipoUsuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaVincularPaciente(
    navController: NavController,
    associadoUid: String,
    associadoTipo: TipoUsuario,
    viewModel: LinkPacienteViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(viewModel.linkSuccess) {
        if (viewModel.linkSuccess) {
            Toast.makeText(context, "Solicitação de vínculo enviada com sucesso!", Toast.LENGTH_SHORT).show()
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
            .background(Color(0xFFE5F4F5))
    ) {
        // Onda superior para consistência visual
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
                    moveTo(0f, h * 0.6f)
                    cubicTo(w * 0.25f, h * 1.2f, w * 0.75f, h * 0.5f, w, h * 0.8f)
                    lineTo(w, 0f)
                    lineTo(0f, 0f)
                    close()
                },
                color = Color(0xFF73A5AD)
            )
        }

        // Conteúdo centralizado em um Card
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.Center)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Vincular Paciente",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Digite o e-mail do paciente para enviar uma solicitação de vinculação.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = Color.Gray
                )
                Spacer(Modifier.height(24.dp))

                OutlinedTextField(
                    value = viewModel.emailPaciente,
                    onValueChange = { viewModel.emailPaciente = it },
                    label = { Text("E-mail do Paciente") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.onLinkPatientClick(associadoUid, associadoTipo) },
                    enabled = !viewModel.isLoading,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42A5F5)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(if (viewModel.isLoading) "Enviando..." else "Enviar Solicitação", color = Color.White)
                }
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        }
    }
}