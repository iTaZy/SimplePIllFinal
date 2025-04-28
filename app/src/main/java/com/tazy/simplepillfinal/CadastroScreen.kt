package com.tazy.simplepillfinal

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.foundation.Image
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.tazy.simplepillfinal.auth.autenticar
import com.tazy.simplepillfinal.model.TipoUsuario




@Composable
fun CadastroScreen(navController: NavHostController) {
    val backgroundColor = Brush.verticalGradient(
        colors = listOf(Color(0xFFF0E68C), Color(0xFFAEEEEE))
    )

    var abaSelecionada by remember { mutableStateOf(0) } // 0 = Cadastro, 1 = Login

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.logo_simple_pill),
                    contentDescription = "Logo Simple Pill",
                    modifier = Modifier.size(190.dp)

                )

            }

            // Abas de Navegação
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 45.dp),
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

            if (abaSelecionada == 0) {
                ConteudoCadastro(navController)
            } else {
                ConteudoLogin(navController)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(text = "ou", fontSize = 16.sp)
                Text(
                    text = "conheça o aplicativo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { }
                )
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
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CadastroButton("Paciente") {
            navController.navigate(AppRoutes.CadastroPaciente)
        }
        CadastroButton("Cuidador") {
            navController.navigate(AppRoutes.CadastroCuidador)
        }
        CadastroButton("Profissional da saúde") {
            navController.navigate(AppRoutes.CadastroProfissional)
        }
    }
}

@Composable
private fun ConteudoLogin(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E‑mail") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                loading = true
                scope.launch {
                    try {
                        val usuario = autenticar(email, senha)
                        when (usuario.tipo) {
                            TipoUsuario.PACIENTE ->
                                navController.navigate("${AppRoutes.BemVindoPaciente}/${usuario.nome}/${email}")
                            TipoUsuario.CUIDADOR ->
                                navController.navigate("${AppRoutes.BemVindoCuidador}/${usuario.nome}/${email}")
                            TipoUsuario.PROFISSIONAL_SAUDE ->
                                navController.navigate("${AppRoutes.BemVindoProfissional}/${usuario.nome}/${email}")
                        }
                    } finally {
                        loading = false
                    }
                }
            },
            enabled = !loading,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E8B57)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(if (loading) "Carregando..." else "Entrar", fontSize = 18.sp, color = Color.White)
        }
    }
}


@Composable
fun CadastroButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(text = text, fontSize = 18.sp, color = Color.Black)
    }
}

