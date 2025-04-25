package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tazy.simplepillfinal.R

@Composable
fun TelaCadastroCuidador(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Cabeçalho verde
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color(0xFF1B7814)), // Verde escuro
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "Cadastro do Cuidador",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 50.dp)
            )
        }

        // Conteúdo branco com cantos arredondados
        Surface(
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 110.dp),
            color = Color.White
        ) {
            FormularioCuidador(navController)
        }
    }
}

@Composable
private fun FormularioCuidador(navController: NavController) {
    var nome by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var profissao by remember { mutableStateOf("") }
    var idade by remember { mutableStateOf("") }
    var endereco by remember { mutableStateOf("") }
    var nomePaciente by remember { mutableStateOf("") }
    var idPerfilPaciente by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        Image(
            painter = painterResource(id = R.drawable.ic_person_placeholder), // troque se precisar
            contentDescription = "Avatar Cuidador",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )

        Spacer(Modifier.height(24.dp))

        // Campos de texto
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = telefone,
            onValueChange = { telefone = it },
            label = { Text("Telefone") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = profissao,
            onValueChange = { profissao = it },
            label = { Text("Profissão") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = idade,
            onValueChange = { idade = it },
            label = { Text("Idade") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = endereco,
            onValueChange = { endereco = it },
            label = { Text("Endereço") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = nomePaciente,
            onValueChange = { nomePaciente = it },
            label = { Text("Nome do paciente") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = idPerfilPaciente,
            onValueChange = { idPerfilPaciente = it },
            label = { Text("ID do perfil do paciente") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        Spacer(Modifier.height(20.dp))

        // Botões
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Voltar",
                fontSize = 20.sp,
                textDecoration = TextDecoration.Underline,
                color = Color.Black,
                modifier = Modifier.clickable { navController.popBackStack() }
            )

            Button(
                onClick = { /* TODO: salvar dados */ },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B7814)),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = "Salvar",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}
