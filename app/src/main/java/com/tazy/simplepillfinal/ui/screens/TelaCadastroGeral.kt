// F_ARQUIVO: ui/screens/TelaCadastroGeral.kt
package com.tazy.simplepillfinal.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tazy.simplepillfinal.model.TipoUsuario
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastroGeral(navController: NavController, viewModel: CadastroGeralViewModel = viewModel()) {
    val context = LocalContext.current

    LaunchedEffect(viewModel.registrationSuccess) {
        if (viewModel.registrationSuccess) {
            Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }
    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            Toast.makeText(context, "Erro: $it", Toast.LENGTH_LONG).show()
        }
    }

    val containerColor = when {
        TipoUsuario.PACIENTE in viewModel.perfisSelecionados -> Color(0xFF73A5AD)
        TipoUsuario.CUIDADOR in viewModel.perfisSelecionados -> Color(0xFF1B7814)
        TipoUsuario.PROFISSIONAL_SAUDE in viewModel.perfisSelecionados -> Color(0xFF4369B9)
        else -> Color(0xFFE5F4F5)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE5F4F5))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(containerColor)
                .align(Alignment.TopCenter)
        ) {
            Text(
                "Cadastro",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(150.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Eu sou:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf(TipoUsuario.PACIENTE, TipoUsuario.CUIDADOR, TipoUsuario.PROFISSIONAL_SAUDE).forEach { tipo ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = tipo in viewModel.perfisSelecionados,
                                onCheckedChange = { viewModel.togglePerfil(tipo) }
                            )
                            Text(
                                text = tipo.name.replace("_", " ").lowercase().replaceFirstChar { it.titlecase() },
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = viewModel.nome,
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text("Nome Completo*") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = { Text("Email*") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            OutlinedTextField(
                value = viewModel.senha,
                onValueChange = { viewModel.senha = it },
                label = { Text("Senha*") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            Text(
                text = "Mínimo de 6 caracteres e uma letra maiúscula.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.cep,
                onValueChange = {
                    viewModel.cep = it
                    if (it.length == 8) {
                        viewModel.buscarEnderecoPorCep(it)
                    }
                },
                label = { Text("CEP") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            OutlinedTextField(
                value = viewModel.endereco,
                onValueChange = { viewModel.endereco = it },
                label = { Text("Endereço") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = viewModel.numero,
                    onValueChange = { viewModel.numero = it },
                    label = { Text("Número") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                )
                OutlinedTextField(
                    value = viewModel.complemento,
                    onValueChange = { viewModel.complemento = it },
                    label = { Text("Complemento") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            OutlinedTextField(
                value = viewModel.telefone,
                onValueChange = { viewModel.onPhoneChange(it) },
                label = { Text("Telefone") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            OutlinedTextField(
                value = viewModel.idade,
                onValueChange = { viewModel.onAgeChange(it) },
                label = { Text("Idade") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            OutlinedTextField(
                value = viewModel.profissao,
                onValueChange = { viewModel.profissao = it },
                label = { Text("Profissão") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            if (TipoUsuario.PACIENTE in viewModel.perfisSelecionados) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Informações de Paciente", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
                OutlinedTextField(
                    value = viewModel.nacionalidade,
                    onValueChange = { viewModel.onNationalityChange(it) },
                    label = { Text("Nacionalidade") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
                OutlinedTextField(
                    value = viewModel.numSus,
                    onValueChange = { viewModel.onSusChange(it) },
                    label = { Text("Nº da carteira do SUS") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
                OutlinedTextField(
                    value = viewModel.unidadeSus,
                    onValueChange = { viewModel.unidadeSus = it },
                    label = { Text("Unidade de atendimento do SUS") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
            }
            // NOVO: Campos para Profissional de Saúde
            if (TipoUsuario.PROFISSIONAL_SAUDE in viewModel.perfisSelecionados) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Informações de Profissional", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
                OutlinedTextField(
                    value = viewModel.crm,
                    onValueChange = { viewModel.onCrmChange(it) },
                    label = { Text("CRM* (Apenas números)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
                OutlinedTextField(
                    value = viewModel.ufCrm,
                    onValueChange = { viewModel.onUfCrmChange(it) },
                    label = { Text("UF do CRM*") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { viewModel.onRegistrationClick() },
                enabled = !viewModel.isLoading,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = containerColor),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    if (viewModel.isLoading) "Cadastrando..." else "Cadastrar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}