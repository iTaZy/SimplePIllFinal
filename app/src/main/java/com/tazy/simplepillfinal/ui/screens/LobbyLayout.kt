// F_ARQUIVO: ui/screens/LobbyLayout.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tazy.simplepillfinal.R
import com.tazy.simplepillfinal.data.AuthRepository
import com.tazy.simplepillfinal.navigation.AppRoutes

@Composable
fun LobbyLayout(
    navController: NavController,
    nome: String,
    backgroundColor: Color, // Usado para a cor de fundo principal
    menuItems: List<Pair<String, () -> Unit>> // Lista de itens do menu (texto e ação)
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5)) // Fundo cinza claro para o corpo
    ) {
        // Onda superior
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp) // Altura reduzida para o cabeçalho
                .align(Alignment.TopCenter)
        ) {
            val w = size.width
            val h = size.height
            drawPath(
                path = Path().apply {
                    moveTo(0f, h * 0.8f) // Começa um pouco mais acima
                    cubicTo(w * 0.25f, h * 1.2f, w * 0.75f, h * 0.5f, w, h * 0.8f)
                    lineTo(w, 0f)
                    lineTo(0f, 0f)
                    close()
                },
                color = backgroundColor // Cor da onda (cor principal do usuário)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp), // Padding superior para o cabeçalho
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Cabeçalho
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bem-vindo,",
                    fontSize = 24.sp,
                    color = Color.White, // Texto branco no cabeçalho
                    fontWeight = FontWeight.Light,
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = nome,
                    fontSize = 28.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
            }

            Spacer(modifier = Modifier.height(32.dp)) // Espaço entre o cabeçalho e os cartões

            // Grade de botões
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2 colunas
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(menuItems) { item ->
                    CardButton(text = item.first, onClick = item.second)
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Empurra o conteúdo para cima e o rodapé para baixo

            // Rodapé com botão Sair e logo
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.clickable {
                        AuthRepository().signOut()
                        navController.navigate(AppRoutes.TELA_INICIAL) {
                            popUpTo(AppRoutes.TELA_INICIAL) { inclusive = true }
                        }
                    },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Sair",
                        tint = Color.Black.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Sair",
                        fontSize = 16.sp,
                        textDecoration = TextDecoration.Underline,
                        color = Color.Black.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.logo_simple_pill),
                    contentDescription = "Logo Simple Pill",
                    modifier = Modifier.size(120.dp) // Tamanho um pouco menor para o logo
                )
            }
        }
    }
}

@Composable
fun CardButton(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp) // Altura padrão para os cartões
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp), // Cantos mais arredondados
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

// As funções LobbyButton e ActionButton não serão mais usadas diretamente neste layout,
// mas podem ser mantidas se forem usadas em outras partes do app.
// Por simplicidade, vou removê-las para evitar confusão, mas você pode decidir mantê-las.
// @Composable
// fun LobbyButton(...) { ... }
// @Composable
// fun ActionButton(...) { ... }