package com.tazy.simplepillfinal.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tazy.simplepillfinal.model.Usuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalhesMedico(
    navController: NavController,
    medicoUid: String,
    viewModel: MedicosVinculadosViewModel = viewModel()
) {
    val context = LocalContext.current
    val selectedMedico = viewModel.selectedMedico

    // Carrega o médico com base no UID passado pela navegação
    LaunchedEffect(medicoUid) {
        viewModel.loadSelectedMedico(medicoUid)
    }

    if (selectedMedico == null) {
        // Exibe um loading ou mensagem de erro enquanto o médico não é carregado
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Médico") },
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
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = selectedMedico.nome,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = selectedMedico.email,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { viewModel.togglePasswordDialog(true) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Desfazer Vínculo")
            }
        }
    }

    if (viewModel.showPasswordDialog) {
        PasswordConfirmationDialog(
            onConfirm = { password ->
                viewModel.desfazerVinculo(medicoUid, password)
                navController.popBackStack()
            },
            onDismiss = { viewModel.togglePasswordDialog(false) },
            isLoading = viewModel.isLoading,
            errorMessage = viewModel.errorMessage
        )
    }

    LaunchedEffect(viewModel.errorMessage) {
        if (viewModel.errorMessage != null) {
            Toast.makeText(context, "Erro: ${viewModel.errorMessage}", Toast.LENGTH_SHORT).show()
        }
    }
}


@Composable
fun PasswordConfirmationDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?
) {
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmação de Segurança") },
        text = {
            Column {
                Text("Para desfazer o vínculo, por favor, insira sua senha.")
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Senha") },
                    singleLine = true,
                    isError = errorMessage != null
                )
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(password) },
                enabled = !isLoading && password.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(Modifier.size(24.dp))
                } else {
                    Text("Confirmar")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}