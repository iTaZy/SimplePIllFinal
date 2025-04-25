// app/src/main/java/com/tazy/simplepillfinal/ui/screens/TelaProximosExamesPaciente.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tazy.simplepillfinal.ui.components.InputCard

@Composable
fun TelaProximosExamesPaciente(navController: NavController) {
    val azul = Color(0xFF7AA7B4)
    val cardBg = Color(0xFFE0DDDC)
    val dividerColor = azul

    // estado de cada campo
    var data by remember { mutableStateOf("") }
    var local by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }
    var especialidade by remember { mutableStateOf("") }
    var preparo by remember { mutableStateOf("") }

    Box(Modifier.fillMaxSize()) {
        // 1) Curva branca no topo
        Canvas(Modifier
            .fillMaxWidth()
            .height(200.dp)
        ) {
            val w = size.width
            val h = size.height
            drawPath(
                path = Path().apply {
                    moveTo(0f, h * 0.6f)
                    cubicTo(
                        w * 0.25f, h,
                        w * 0.75f, h * 0.2f,
                        w, h * 0.6f
                    )
                    lineTo(w, 0f); lineTo(0f, 0f); close()
                },
                color = Color.White
            )
        }

        // 2) Conteúdo sobre fundo azul
        Column(
            Modifier
                .fillMaxSize()
                .background(azul)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Próximos exames",
                fontSize = 28.sp,
                color = Color.Black
            )

            Spacer(Modifier.height(24.dp))

            // Campos editáveis
            InputCard(
                label = "Data",
                value = data,
                onValueChange = { data = it },
                background = cardBg,
                dividerColor = dividerColor
            )
            InputCard(
                label = "Local",
                value = local,
                onValueChange = { local = it },
                background = cardBg,
                dividerColor = dividerColor
            )
            InputCard(
                label = "Horário",
                value = horario,
                onValueChange = { horario = it },
                background = cardBg,
                dividerColor = dividerColor
            ) {

            }
            InputCard(
                label = "Especialidade",
                value = especialidade,
                onValueChange = { especialidade = it },
                background = cardBg,
                dividerColor = dividerColor
            )
            InputCard(
                label = "Preparo",
                value = preparo,
                onValueChange = { preparo = it },
                background = cardBg,
                dividerColor = dividerColor
            )

            Spacer(Modifier.weight(1f))

            // 3) Rodapé com Voltar e Salvar
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { navController.popBackStack() }) {
                    Text(
                        "Voltar",
                        fontSize = 16.sp,
                        color = Color.White,
                        textDecoration = TextDecoration.Underline
                    )
                }
                Button(
                    onClick = { /* TODO: salvar dados */ },
                    colors = ButtonDefaults.buttonColors(containerColor = azul),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text("Salvar", color = Color.White, fontSize = 18.sp)
                }
            }
        }
    }
}


