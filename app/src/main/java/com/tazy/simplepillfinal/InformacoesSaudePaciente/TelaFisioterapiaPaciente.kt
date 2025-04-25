// app/src/main/java/com/tazy/simplepillfinal/ui/screens/TelaFisioterapiaPaciente.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tazy.simplepillfinal.AppRoutes
import com.tazy.simplepillfinal.R
import com.tazy.simplepillfinal.ui.components.BotaoOval

@Composable
fun TelaFisioterapiaPaciente(navController: NavController) {
    val fundoAzul = Color(0xFF7AA7B4)

    Box(Modifier.fillMaxSize()) {
        // 1) Curva branca
        Canvas(
            Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            val w = size.width
            val h = size.height
            drawPath(
                path = Path().apply {
                    moveTo(0f, h * 0.7f)
                    cubicTo(
                        w * 0.25f, h,
                        w * 0.75f, h * 0.2f,
                        w, h * 0.7f
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
                .background(fundoAzul)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = "Sua seção de fisioterapia",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(Modifier.height(32.dp))

            // Botão oval
            BotaoOval(texto = "Próximas seções") {
                navController.navigate(AppRoutes.TelaProximasFisioterapiaPaciente)
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
                text = "Voltar",
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
