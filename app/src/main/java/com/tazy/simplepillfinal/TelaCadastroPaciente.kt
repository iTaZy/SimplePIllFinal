package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun TelaCadastroPaciente(navController: NavController) {
    Box(Modifier.fillMaxSize()) {
        // Cabeçalho colorido
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color(0xFF73A5AD)),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "Cadastro do Paciente",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 50.dp)
            )
        }

        // Container branco “flutuante”
        Surface(
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 110.dp),
            color = Color.White
        ) {
            FormularioCadastro(navController)
        }
    }
}

@Composable
private fun FormularioCadastro(navController: NavController) {
    var nome by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var profissao by remember { mutableStateOf("") }
    var idade by remember { mutableStateOf("") }
    var endereco by remember { mutableStateOf("") }
    var nacionalidade by remember { mutableStateOf("") }
    var numSus by remember { mutableStateOf("") }
    var unidadeSus by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        Image(
            painter = painterResource(id = R.drawable.ic_person_placeholder),
            contentDescription = "Avatar do Paciente",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        )

        Spacer(Modifier.height(24.dp))

        // Campos de texto usando cores e estilos padrão
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = telefone,
            onValueChange = { telefone = it },
            label = { Text("Telefone") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = profissao,
            onValueChange = { profissao = it },
            label = { Text("Profissão") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = idade,
            onValueChange = { idade = it },
            label = { Text("Idade") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = endereco,
            onValueChange = { endereco = it },
            label = { Text("Endereço") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = nacionalidade,
            onValueChange = { nacionalidade = it },
            label = { Text("Nacionalidade") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = numSus,
            onValueChange = { numSus = it },
            label = { Text("N° da carteira do SUS") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = unidadeSus,
            onValueChange = { unidadeSus = it },
            label = { Text("Unidade de atendimento do SUS") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )

        Spacer(Modifier.height(15.dp))

        // Botões Voltar e Salvar
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
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
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF73A5AD)),
                modifier = Modifier.height(48.dp)

            ) {
                Text(
                    text = "Salvar",
                    color = Color.White,
                    fontSize = 18.sp,

                )
            }
        }
    }
}
