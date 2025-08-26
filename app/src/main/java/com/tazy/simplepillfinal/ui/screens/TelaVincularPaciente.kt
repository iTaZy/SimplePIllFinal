// CRIE ESTE NOVO ARQUIVO: ui/screens/TelaVincularPaciente.kt
package com.tazy.simplepillfinal.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tazy.simplepillfinal.model.TipoUsuario

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
            Toast.makeText(context, "Paciente vinculado com sucesso!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }
    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            Toast.makeText(context, "Erro: $it", Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Vincular Paciente", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            "Digite o e-mail do paciente que já possui cadastro no Simple Pill.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = viewModel.emailPaciente,
            onValueChange = { viewModel.emailPaciente = it },
            label = { Text("E-mail do Paciente") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { viewModel.onLinkPatientClick(associadoUid, associadoTipo) },
            enabled = !viewModel.isLoading,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(if (viewModel.isLoading) "Vinculando..." else "Vincular")
        }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = { navController.popBackStack() }) {
            Text("Cancelar")
        }
    }
}