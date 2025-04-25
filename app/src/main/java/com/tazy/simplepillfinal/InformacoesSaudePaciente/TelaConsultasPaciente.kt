package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tazy.simplepillfinal.R
import com.tazy.simplepillfinal.ui.components.BotaoOval
import com.tazy.simplepillfinal.AppRoutes


@Composable
fun TelaConsultasPaciente(navController: NavController) {
    val azulFundo = Color(0xFF7AA7B4)

    Box(Modifier.fillMaxSize()) {
        // 1) Curva branca no topo
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
        ) {
            val w = size.width
            val h = size.height
            drawPath(
                Path().apply {
                    moveTo(0f, h * 0.7f)
                    cubicTo(
                        w * 0.25f, h,
                        w * 0.75f, h * 0.2f,
                        w, h * 0.7f
                    )
                    lineTo(w, 0f)
                    lineTo(0f, 0f)
                    close()
                },
                color = Color.White
            )
        }

        // 2) Fundo azul e conteúdo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(azulFundo)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))

            // Título
            Text(
                "Suas Consultas",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(Modifier.height(32.dp))

            // Botões ovais
            BotaoOval("Próximas consultas") {
                navController.navigate(AppRoutes.TelaProximasConsultasPaciente)
            }
            BotaoOval("Anteriores") {
                navController.navigate("tela_consultas_anteriores_paciente")
            }

            Spacer(Modifier.weight(1f))

            // Rodapé: logo + voltar
            Image(
                painter = painterResource(id = R.drawable.logo_simple_pill),
                contentDescription = "Logo Simple Pill",
                modifier = Modifier.size(100.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Voltar",
                fontSize = 16.sp,
                textDecoration = TextDecoration.Underline,
                color = Color.Black,
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .padding(top = 8.dp)
            )
        }
    }
}

