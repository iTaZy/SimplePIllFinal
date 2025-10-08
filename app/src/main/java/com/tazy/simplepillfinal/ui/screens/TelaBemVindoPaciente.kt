// F_ARQUIVO: ui/screens/TelaBemVindoPaciente.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.tazy.simplepillfinal.navigation.AppRoutes
import com.tazy.simplepillfinal.ui.components.BottomNavigationBar // Importar o novo Composable
import com.tazy.simplepillfinal.model.TipoUsuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaBemVindoPaciente(navController: NavController, nome: String, email: String, uid: String) {
    val backgroundColor = Color(0xFF74ABBF)

    // Usar Scaffold para adicionar a barra de navegação inferior
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, uid = uid, tipo = TipoUsuario.PACIENTE)
        }
    ) { paddingValues ->
        // O conteúdo da tela deve ser aninhado dentro deste Box
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F2F5))
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            LobbyLayout(
                navController = navController,
                nome = nome,
                backgroundColor = backgroundColor,
                menuItems = listOf(
                    Pair("Minhas Medicações") { navController.navigate("${AppRoutes.MINHAS_MEDICACOES}/$uid") },
                    Pair("Exames") { navController.navigate("${AppRoutes.VISUALIZAR_EXAMES}/$uid") },
                    Pair("Vacinação") { navController.navigate("${AppRoutes.VISUALIZAR_VACINACAO}/$uid") },
                    Pair("Internações") { navController.navigate("${AppRoutes.VISUALIZAR_INTERNACOES}/$uid") },
                    Pair("Fisioterapia") { navController.navigate("${AppRoutes.VISUALIZAR_FISIOTERAPIA}/$uid") },
                    Pair("Saúde mental") { navController.navigate("${AppRoutes.VISUALIZAR_SAUDE_MENTAL}/$uid") },
                    Pair("Nutrição") { navController.navigate("${AppRoutes.VISUALIZAR_NUTRICAO}/$uid") }
                )
            )
        }
    }
}