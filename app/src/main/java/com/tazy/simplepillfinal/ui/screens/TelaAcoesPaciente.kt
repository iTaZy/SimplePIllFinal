// CRIE ESTE NOVO ARQUIVO: ui/screens/TelaAcoesPaciente.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tazy.simplepillfinal.R
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun TelaAcoesPaciente(
    navController: NavController,
    pacienteUid: String, // PARÂMETRO ADICIONADO
    pacienteNome: String
) {
    // Decodifica o nome do paciente, caso ele contenha espaços ou caracteres especiais
    val decodedPacienteNome = remember(pacienteNome) {
        try {
            URLDecoder.decode(pacienteNome, StandardCharsets.UTF_8.toString())
        } catch (e: Exception) {
            pacienteNome // fallback
        }
    }

    val backgroundColor = Color(0xFFE2C64D) // Amarelo da imagem de referência

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cabeçalho com nome do paciente e foto
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Paciente,\n$decodedPacienteNome",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                lineHeight = 36.sp
            )
            Image(
                painter = painterResource(id = R.drawable.ic_person_placeholder),
                contentDescription = "Foto do Paciente",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        // Botões de Ação
        val actions = listOf(
            "Solicitar exames", "Prescrever medicações", "Solicitar vacinação",
            "Registrar internação", "Cadastro fisioterapêutico", "Cadastro saúde mental",
            "Cadastro nutricional"
        )

        actions.forEach { action ->
            ActionButton(text = action) {
                // TODO: Implementar a lógica de navegação ou ação para cada botão
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botão Voltar
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { navController.popBackStack() }
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_simple_pill),
                contentDescription = "Voltar",
                modifier = Modifier.size(60.dp)
            )
            Text(
                text = "Voltar",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ActionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(50.dp)
    ) {
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}