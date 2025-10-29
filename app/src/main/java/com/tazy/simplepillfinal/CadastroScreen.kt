// F_ARQUIVO: CadastroScreen.kt
package com.tazy.simplepillfinal

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tazy.simplepillfinal.model.TipoUsuario
import com.tazy.simplepillfinal.navigation.AppRoutes
import com.tazy.simplepillfinal.ui.screens.LoginViewModel
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CadastroScreen(navController: NavHostController) {
    val backgroundColor = Brush.verticalGradient(
        colors = listOf(Color(0xFFE5F4F5), Color(0xFFF0F2F5))
    )

    var abaSelecionada by remember { mutableStateOf(1) } // 0 = Cadastro, 1 = Login

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_simple_pill),
                contentDescription = "Logo Simple Pill",
                modifier = Modifier.size(190.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AbaSelecionavel(
                    texto = "Cadastro",
                    selecionado = abaSelecionada == 0,
                    aoClicar = { abaSelecionada = 0 }
                )
                AbaSelecionavel(
                    texto = "Login",
                    selecionado = abaSelecionada == 1,
                    aoClicar = { abaSelecionada = 1 }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (abaSelecionada == 0) {
                ConteudoCadastro(navController)
            } else {
                ConteudoLogin(navController)
            }
        }
    }
}

@Composable
fun AbaSelecionavel(texto: String, selecionado: Boolean, aoClicar: () -> Unit) {
    Text(
        text = texto,
        fontSize = 20.sp,
        fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal,
        color = if (selecionado) Color(0xFF2E8B57) else Color.Gray,
        modifier = Modifier.clickable(onClick = aoClicar)
    )
}

@Composable
fun ConteudoCadastro(navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Crie sua conta e comece a gerenciar sua saúde!", textAlign = TextAlign.Center)
        Button(
            onClick = { navController.navigate(AppRoutes.CADASTRO_GERAL) },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E8B57)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(text = "Criar nova conta", fontSize = 18.sp, color = Color.White)
        }
    }
}

@Composable
private fun ConteudoLogin(navController: NavHostController, viewModel: LoginViewModel = viewModel()) {
    val context = LocalContext.current

    LaunchedEffect(viewModel.loginSuccessUser) {
        val user = viewModel.loginSuccessUser
        if (user != null) {
            val route = when (user.tipo) {
                TipoUsuario.PACIENTE -> "${AppRoutes.BEM_VINDO_PACIENTE}/${user.uid}"
                TipoUsuario.CUIDADOR -> "${AppRoutes.BEM_VINDO_CUIDADOR}/${user.uid}"
                TipoUsuario.PROFISSIONAL_SAUDE -> "${AppRoutes.BEM_VINDO_PROFISSIONAL}/${user.uid}"
            }
            navController.navigate(route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            Toast.makeText(context, "Erro: $it", Toast.LENGTH_LONG).show()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text("E‑mail") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2E8B57),
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color(0xFF2E8B57)
            )
        )
        OutlinedTextField(
            value = viewModel.senha,
            onValueChange = { viewModel.senha = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2E8B57),
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color(0xFF2E8B57)
            )
        )

        Text("Logar como:", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf(TipoUsuario.PACIENTE, TipoUsuario.CUIDADOR, TipoUsuario.PROFISSIONAL_SAUDE).forEach { tipo ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = viewModel.tipoUsuario == tipo,
                        onClick = { viewModel.tipoUsuario = tipo }
                    )
                    val tipoNome = tipo.name.replace("_", " ").lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    Text(text = tipoNome, fontSize = 14.sp)
                }
            }
        }

        Button(
            onClick = { viewModel.onLoginClick() },
            enabled = !viewModel.isLoading,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E8B57)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(if (viewModel.isLoading) "Carregando..." else "Entrar", fontSize = 18.sp, color = Color.White)
        }

        // --- TEXTO "CONHEÇA O APLICATIVO" REMOVIDO DAQUI ---
    }
}