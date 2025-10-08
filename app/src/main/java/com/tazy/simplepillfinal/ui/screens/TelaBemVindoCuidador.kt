// F_ARQUIVO: ui/screens/TelaBemVindoCuidador.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.tazy.simplepillfinal.model.TipoUsuario
import com.tazy.simplepillfinal.navigation.AppRoutes
import com.tazy.simplepillfinal.ui.components.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaBemVindoCuidador(navController: NavController, nome: String, uid: String) {
    val backgroundColor = Color(0xFF166A1E)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, uid = uid, tipo = TipoUsuario.CUIDADOR)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LobbyLayout(
                navController = navController,
                nome = nome,
                backgroundColor = backgroundColor,
                menuItems = listOf(
                    Pair("Pacientes Vinculados") { navController.navigate("${AppRoutes.PACIENTES_VINCULADOS}/$uid/${TipoUsuario.CUIDADOR}") },
                    Pair("Vincular Paciente") { navController.navigate("${AppRoutes.VINCULAR_PACIENTE}/$uid/${TipoUsuario.CUIDADOR}") }
                )
            )
        }
    }
}