// ARQUIVO RENOMEADO E CORRIGIDO: ui/screens/TelaPacienteAcoes.kt
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
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * Tela genérica refatorada para exibir ações (Profissional) ou visualizações (Cuidador).
 */
@Composable
fun TelaPacienteAcoes(
    navController: NavController,
    pacienteNome: String,
    backgroundColor: Color, // Cor de fundo da tela
    contentColor: Color, // Cor do conteúdo (textos)
    menuItems: List<Pair<String, () -> Unit>> // Lista de botões (Texto, Ação)
) {
    val decodedPacienteNome = remember(pacienteNome) {
        try {
            URLDecoder.decode(pacienteNome, StandardCharsets.UTF_8.toString())
        } catch (e: Exception) {
            pacienteNome
        }
    }

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
                color = contentColor,
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

        // Botões de ação como cartões individuais
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Itera sobre a lista de itens do menu
            menuItems.forEach { (text, onClick) ->
                ActionButton(text = text, onClick = onClick)
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // Espaço entre os botões e o rodapé

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
                color = contentColor,
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