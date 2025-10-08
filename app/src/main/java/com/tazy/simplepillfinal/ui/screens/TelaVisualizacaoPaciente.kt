// F_ARQUIVO: ui/screens/TelaVisualizacaoPaciente.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import java.nio.charset.StandardCharsets

@Composable
fun TelaVisualizacaoPaciente(
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

    val backgroundColor = Color(0xFF166A1E)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
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
                color = Color.White,
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

        // Botões de ação como cartões individuais para visualização
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ActionButton(text = "Visualizar Medicações") {
                navController.navigate("${AppRoutes.MINHAS_MEDICACOES}/$pacienteUid")
            }

            ActionButton(text = "Visualizar Exames") {
                navController.navigate("${AppRoutes.VISUALIZAR_EXAMES}/$pacienteUid")
            }

            ActionButton(text = "Visualizar Vacinação") {
                navController.navigate("${AppRoutes.VISUALIZAR_VACINACAO}/$pacienteUid")
            }

            ActionButton(text = "Visualizar Internações") {
                navController.navigate("${AppRoutes.VISUALIZAR_INTERNACOES}/$pacienteUid")
            }

            ActionButton(text = "Visualizar Fisioterapia") {
                navController.navigate("${AppRoutes.VISUALIZAR_FISIOTERAPIA}/$pacienteUid")
            }

            ActionButton(text = "Visualizar Saúde Mental") {
                navController.navigate("${AppRoutes.VISUALIZAR_SAUDE_MENTAL}/$pacienteUid")
            }

            ActionButton(text = "Visualizar Nutrição") {
                navController.navigate("${AppRoutes.VISUALIZAR_NUTRICAO}/$pacienteUid")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ActionButton(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .height(56.dp)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}