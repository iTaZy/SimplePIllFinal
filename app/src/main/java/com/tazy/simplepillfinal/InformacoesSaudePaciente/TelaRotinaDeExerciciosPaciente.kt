// app/src/main/java/com/tazy/simplepillfinal/ui/screens/TelaRotinaDeExerciciosPaciente.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tazy.simplepillfinal.AppRoutes
import com.tazy.simplepillfinal.R
import com.tazy.simplepillfinal.ui.components.BotaoOval
import com.tazy.simplepillfinal.data.DataStoreManager

@Composable
fun TelaRotinaDeExerciciosPaciente(navController: NavController) {
    val fundoAzul = Color(0xFF7AA7B4)
    val dias = listOf("Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado")

    // Instancia o DataStoreManager
    val context = LocalContext.current
    val dsManager = remember { DataStoreManager(context) }

    Box(Modifier.fillMaxSize()) {
        // 1) Curva branca no topo
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            val w = size.width
            val h = size.height
            drawPath(
                path = Path().apply {
                    moveTo(0f, h * 0.7f)
                    cubicTo(w * 0.25f, h, w * 0.75f, h * 0.2f, w, h * 0.7f)
                    lineTo(w, 0f)
                    lineTo(0f, 0f)
                    close()
                },
                color = Color.White
            )
        }

        // 2) Conteúdo sobre fundo azul
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(fundoAzul)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Sua rotina de exercícios",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(Modifier.height(32.dp))

            // 3) Para cada dia, coletamos a lista e mostramos a contagem
            dias.forEach { dia ->
                val exercicios by dsManager
                    .getExercicios(dia)
                    .collectAsState(initial = emptyList())

                BotaoOval(
                    texto = "$dia (${exercicios.size})",
                    onClick = {
                        navController.navigate("${AppRoutes.TelaExerciciosDiaPaciente}/$dia")
                    }
                )
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.weight(1f))

            // 4) Rodapé com logo e voltar
            Image(
                painter = painterResource(id = R.drawable.logo_simple_pill),
                contentDescription = "Logo Simple Pill",
                modifier = Modifier.size(100.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Voltar",
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .padding(top = 8.dp)
            )
        }
    }
}
