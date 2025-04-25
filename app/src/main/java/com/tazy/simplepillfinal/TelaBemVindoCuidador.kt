package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tazy.simplepillfinal.R

@Composable
fun TelaBemVindoCuidador(
    navController: NavHostController,
    nome: String
) {
    // verde escuro para cuidador
    val darkGreen = Color(0xFF166A1E)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkGreen)
    ) {
        // Curva branca no topo
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
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
                    lineTo(w, 0f)
                    lineTo(0f, 0f)
                    close()
                },
                color = Color.White
            )
        }

        // Conteúdo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Bem‑vindo,",
                fontSize = 32.sp,

                color = Color.Black
            )
            Text(
                text = nome,
                fontSize = 26.sp,

                color = Color.Black
            )

            Spacer(modifier = Modifier.height(200.dp))

            // Botão 1
            Button(
                onClick = { /* navController.navigate("telaInfoSaude") */ },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(
                    "Informações de saúde",
                    fontSize = 20.sp,

                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botão 2
            Button(
                onClick = { /* navController.navigate("telaCadastroItem") */ },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(
                    "Cadastrar itens",
                    fontSize = 20.sp,

                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Ícone de pílula
            Image(
                painter = painterResource(id = R.drawable.logo_simple_pill),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )

            // Voltar sublinhado
            Text(
                text = "Voltar",
                fontSize = 18.sp,

                textDecoration = TextDecoration.Underline,
                color = Color.Black,
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .padding(top = 8.dp)
            )
        }
    }
}
