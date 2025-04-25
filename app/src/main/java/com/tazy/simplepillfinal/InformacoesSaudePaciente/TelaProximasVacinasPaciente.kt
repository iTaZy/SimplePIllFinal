// app/src/main/java/com/tazy/simplepillfinal/ui/screens/TelaProximasVacinasPaciente.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
fun TelaProximasVacinasPaciente(navController: NavController) {
    val azul = Color(0xFF7AA7B4)
    val cardBg = Color(0xFFE0DDDC)
    val dividerColor = azul

    var data by remember { mutableStateOf("") }
    var vacina by remember { mutableStateOf("") }
    var dose by remember { mutableStateOf("") }
    var local by remember { mutableStateOf("") }

    Box(Modifier.fillMaxSize()) {
        Canvas(Modifier
            .fillMaxWidth()
            .height(200.dp)
        ) {
            val w = size.width
            val h = size.height
            drawPath(
                Path().apply {
                    moveTo(0f, h * 0.6f)
                    cubicTo(w * 0.25f, h, w * 0.75f, h * 0.2f, w, h * 0.6f)
                    lineTo(w, 0f); lineTo(0f, 0f); close()
                },
                color = Color.White
            )
        }

        Column(
            Modifier
                .fillMaxSize()
                .background(azul)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Próximas vacinas",
                fontSize = 28.sp,
                color = Color.Black
            )

            Spacer(Modifier.height(24.dp))

            InputCard(
                label = "Data",
                value = data,
                onValueChange = { data = it },
                background = cardBg,
                dividerColor = dividerColor
            )
            InputCard(
                label = "Vacina",
                value = vacina,
                onValueChange = { vacina = it },
                background = cardBg,
                dividerColor = dividerColor
            )
            InputCard(
                label = "Dose",
                value = dose,
                onValueChange = { dose = it },
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

            Spacer(Modifier.weight(1f))

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
