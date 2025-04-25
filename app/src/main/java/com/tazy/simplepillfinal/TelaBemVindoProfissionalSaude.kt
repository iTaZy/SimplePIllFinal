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
import com.tazy.simplepillfinal.AppRoutes

@Composable
fun TelaBemVindoProfissionalSaude(
    navController: NavHostController,
    nome: String
) {
    val mustard = Color(0xFFE2C64D)

    Box(modifier = Modifier
        .fillMaxSize()
        .background(mustard)) {

        // 1) Curva branca no topo
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)) {
            val width = size.width
            val height = size.height
            drawPath(
                path = Path().apply {
                    moveTo(0f, height * 0.6f)
                    cubicTo(
                        width * 0.25f, height,
                        width * 0.75f, height * 0.2f,
                        width, height * 0.6f
                    )
                    lineTo(width, 0f)
                    lineTo(0f, 0f)
                    close()
                },
                color = Color.White
            )
        }

        // 2) Conteúdo principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Bem-vindo,",
                fontSize = 32.sp,

                color = Color.Black
            )
            Text(
                text = nome,
                fontSize = 26.sp,

                color = Color.Black
            )

            Spacer(modifier = Modifier.height(200.dp))

            Button(
                onClick = { /* navController.navigate("telaPacientes") */ },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text("Pacientes", fontSize = 20.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {  navController.navigate(AppRoutes.TelaCadastroPacienteProf) },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text("Cadastrar pacientes", fontSize = 20.sp,  color = Color.Black)
            }

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.logo_simple_pill),
                contentDescription = "Pílula",
                modifier = Modifier.size(150.dp)
            )

            Text(
                text = "Voltar",
                fontSize = 18.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .padding(top = 8.dp),
                color = Color.Black
            )
        }
    }
}
