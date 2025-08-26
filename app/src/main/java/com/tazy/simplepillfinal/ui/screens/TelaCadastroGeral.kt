// F_ARQUIVO: ui/screens/TelaCadastroGeral.kt
package com.tazy.simplepillfinal.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tazy.simplepillfinal.model.TipoUsuario

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

    // Define a cor de fundo com base no perfil selecionado
    val containerColor = when {
        TipoUsuario.PACIENTE in viewModel.perfisSelecionados -> Color(0xFF73A5AD) // Azul para paciente
        TipoUsuario.CUIDADOR in viewModel.perfisSelecionados -> Color(0xFF1B7814) // Verde para cuidador
        TipoUsuario.PROFISSIONAL_SAUDE in viewModel.perfisSelecionados -> Color(0xFFE2C64D) // Amarelo para profissional
        else -> Color(0xFFE5F4F5) // Cor padrão neutra
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // CURVA DE FUNDO
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)) {
            val width = size.width
            val height = size.height
            drawPath(
                path = Path().apply {
                    moveTo(0f, height * 0.7f)
                    cubicTo(
                        width * 0.25f, height,
                        width * 0.75f, height * 0.2f,
                        width, height * 0.7f
                    )
                    lineTo(width, 0f)
                    lineTo(0f, 0f)
                    close()
                },
                color = containerColor
            )
        }

        // CONTEÚDO PRINCIPAL
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(50.dp))
            Text(
                "Cadastro",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(Modifier.height(25.dp))

            // SELEÇÃO DE PERFIS COM CHECKBOXES
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
                        Text(text = tipo.name.replace("_", " ").lowercase().capitalize(), fontSize = 14.sp)
                    }
                }
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Campos Comuns a todos
            OutlinedTextField(value = viewModel.nome, onValueChange = { viewModel.nome = it }, label = { Text("Nome Completo*") })
            OutlinedTextField(value = viewModel.email, onValueChange = { viewModel.email = it }, label = { Text("Email*") })
            OutlinedTextField(value = viewModel.senha, onValueChange = { viewModel.senha = it }, label = { Text("Senha*") }, visualTransformation = PasswordVisualTransformation())
            OutlinedTextField(value = viewModel.telefone, onValueChange = { viewModel.telefone = it }, label = { Text("Telefone") })
            OutlinedTextField(value = viewModel.idade, onValueChange = { viewModel.idade = it }, label = { Text("Idade") })
            OutlinedTextField(value = viewModel.endereco, onValueChange = { viewModel.endereco = it }, label = { Text("Endereço") })
            OutlinedTextField(value = viewModel.profissao, onValueChange = { viewModel.profissao = it }, label = { Text("Profissão") })

            // Campos Condicionais para Paciente
            if (TipoUsuario.PACIENTE in viewModel.perfisSelecionados) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Informações de Paciente", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(value = viewModel.nacionalidade, onValueChange = { viewModel.nacionalidade = it }, label = { Text("Nacionalidade") })
                OutlinedTextField(value = viewModel.numSus, onValueChange = { viewModel.numSus = it }, label = { Text("Nº da carteira do SUS") })
                OutlinedTextField(value = viewModel.unidadeSus, onValueChange = { viewModel.unidadeSus = it }, label = { Text("Unidade de atendimento do SUS") })
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Voltar",
                    fontSize = 18.sp,
                    textDecoration = TextDecoration.Underline,
                    color = Color.Black,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )

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
            }
        }
    }
}