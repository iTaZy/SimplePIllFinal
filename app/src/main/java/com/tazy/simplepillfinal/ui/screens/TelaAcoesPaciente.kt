// F_ARQUIVO: ui/screens/TelaAcoesPaciente.kt
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
import com.tazy.simplepillfinal.navigation.AppRoutes
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun TelaAcoesPaciente(
    navController: NavController,
    pacienteUid: String,
    pacienteNome: String
) {
    val decodedPacienteNome = remember(pacienteNome) {
        try {
            URLDecoder.decode(pacienteNome, StandardCharsets.UTF_8.toString())
        } catch (e: Exception) {
            pacienteNome
        }
    }

    val backgroundColor = Color(0xFFE2C64D)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

        ActionButton(text = "Prescrever medicações") {
            val encodedNome = URLEncoder.encode(decodedPacienteNome, StandardCharsets.UTF_8.toString())
            navController.navigate("${AppRoutes.PRESCREVER_MEDICACAO}/$pacienteUid/$encodedNome")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Botões que navegam para a nova tela unificada, passando o UID
        ActionButton(text = "Solicitar exames") {
            val encodedNome = URLEncoder.encode(decodedPacienteNome, StandardCharsets.UTF_8.toString())
            val encodedAcao = URLEncoder.encode("Exames", StandardCharsets.UTF_8.toString())
            navController.navigate("${AppRoutes.CADASTRO_UNIFICADO}/$pacienteUid/$encodedNome/$encodedAcao")
        }
        Spacer(modifier = Modifier.height(16.dp))

        ActionButton(text = "Solicitar vacinação") {
            val encodedNome = URLEncoder.encode(decodedPacienteNome, StandardCharsets.UTF_8.toString())
            val encodedAcao = URLEncoder.encode("Vacinação", StandardCharsets.UTF_8.toString())
            navController.navigate("${AppRoutes.CADASTRO_UNIFICADO}/$pacienteUid/$encodedNome/$encodedAcao")
        }
        Spacer(modifier = Modifier.height(16.dp))

        ActionButton(text = "Registrar internação") {
            val encodedNome = URLEncoder.encode(decodedPacienteNome, StandardCharsets.UTF_8.toString())
            val encodedAcao = URLEncoder.encode("Internação", StandardCharsets.UTF_8.toString())
            navController.navigate("${AppRoutes.CADASTRO_UNIFICADO}/$pacienteUid/$encodedNome/$encodedAcao")
        }
        Spacer(modifier = Modifier.height(16.dp))

        ActionButton(text = "Cadastro fisioterapêutico") {
            val encodedNome = URLEncoder.encode(decodedPacienteNome, StandardCharsets.UTF_8.toString())
            val encodedAcao = URLEncoder.encode("Fisioterapia", StandardCharsets.UTF_8.toString())
            navController.navigate("${AppRoutes.CADASTRO_UNIFICADO}/$pacienteUid/$encodedNome/$encodedAcao")
        }
        Spacer(modifier = Modifier.height(16.dp))

        ActionButton(text = "Cadastro saúde mental") {
            val encodedNome = URLEncoder.encode(decodedPacienteNome, StandardCharsets.UTF_8.toString())
            val encodedAcao = URLEncoder.encode("Saúde mental", StandardCharsets.UTF_8.toString())
            navController.navigate("${AppRoutes.CADASTRO_UNIFICADO}/$pacienteUid/$encodedNome/$encodedAcao")
        }
        Spacer(modifier = Modifier.height(16.dp))

        ActionButton(text = "Cadastro nutricional") {
            val encodedNome = URLEncoder.encode(decodedPacienteNome, StandardCharsets.UTF_8.toString())
            val encodedAcao = URLEncoder.encode("Nutrição", StandardCharsets.UTF_8.toString())
            navController.navigate("${AppRoutes.CADASTRO_UNIFICADO}/$pacienteUid/$encodedNome/$encodedAcao")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.weight(1f))

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