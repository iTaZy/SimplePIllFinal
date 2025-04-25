package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tazy.simplepillfinal.R
import androidx.compose.ui.text.style.TextDecoration
import com.tazy.simplepillfinal.ui.components.CampoTextoPersonalizado

@Composable
fun TelaCadastroPacienteProf(navController: NavController) {
    val mustard = Color(0xFFE2C64D)

    var nome by remember { mutableStateOf("") }
    var idade by remember { mutableStateOf("") }
    var condicoes by remember { mutableStateOf("") }
    var medicacoes by remember { mutableStateOf("") }
    var consultas by remember { mutableStateOf("") }
    var internacoes by remember { mutableStateOf("") }
    var vacinas by remember { mutableStateOf("") }
    var prontuarios by remember { mutableStateOf("") }
    var sintomas by remember { mutableStateOf("") }
    var observacoes by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fundo com curva amarela
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
                color = mustard
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(50.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_person_placeholder),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
            Spacer(Modifier.height(25.dp))

            CampoTextoPersonalizado("Nome", nome) { nome = it }
            CampoTextoPersonalizado("Idade", idade) { idade = it }
            CampoTextoPersonalizado("Condições de saúde", condicoes) { condicoes = it }
            CampoTextoPersonalizado("Medicações", medicacoes) { medicacoes = it }
            CampoTextoPersonalizado("Consultas", consultas) { consultas = it }
            CampoTextoPersonalizado("Internações", internacoes) { internacoes = it }
            CampoTextoPersonalizado("Vacinas", vacinas) { vacinas = it }
            CampoTextoPersonalizado("Prontuários", prontuarios) { prontuarios = it }
            CampoTextoPersonalizado("Sintomas", sintomas) { sintomas = it }
            CampoTextoPersonalizado("Observações", observacoes) { observacoes = it }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { navController.popBackStack() }) {
                    Text(
                        text = "Voltar",
                        fontSize = 16.sp,
                        textDecoration = TextDecoration.Underline,
                        color = Color.Black
                    )
                }

                Button(
                    onClick = { /* salvar dados */ },
                    colors = ButtonDefaults.buttonColors(containerColor = mustard),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text("Salvar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}




