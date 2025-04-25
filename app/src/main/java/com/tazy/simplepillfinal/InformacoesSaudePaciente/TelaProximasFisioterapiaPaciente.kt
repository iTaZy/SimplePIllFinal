// app/src/main/java/com/tazy/simplepillfinal/ui/screens/TelaProximaFisioterapiaPaciente.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController
import com.tazy.simplepillfinal.R
import com.tazy.simplepillfinal.ui.components.InputCard

@Composable
fun TelaProximasFisioterapiaPaciente(navController: NavController) {
    val azul = Color(0xFF7AA7B4)
    val cardBg = Color(0xFFE0DDDC)
    val dividerColor = azul

    // estados de cada campo
    var data by remember { mutableStateOf("") }
    var local by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }
    var preparo by remember { mutableStateOf("") }

    Box(Modifier.fillMaxSize()) {
        // 1) Curva branca
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
                text = "Próxima fisioterapia",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(Modifier.height(24.dp))

            // Inputs
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
            )
            InputCard(
                label = "Preparo",
                value = preparo,
                onValueChange = { preparo = it },
                background = cardBg,
                dividerColor = dividerColor
            )

            Spacer(Modifier.weight(1f))

            // Rodapé: voltar + salvar
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { navController.popBackStack() }) {
                    Text(
                        "Voltar",
                        fontSize = 16.sp,
                        textDecoration = TextDecoration.Underline,
                        color = Color.White
                    )
                }
                Button(
                    onClick = { /* salvar ação */ },
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
