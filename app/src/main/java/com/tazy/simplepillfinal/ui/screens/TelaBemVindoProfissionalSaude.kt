// F_ARQUIVO: ui/screens/TelaBemVindoProfissionalSaude.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tazy.simplepillfinal.model.TipoUsuario
import com.tazy.simplepillfinal.navigation.AppRoutes

@Composable
fun TelaBemVindoProfissionalSaude(navController: NavController, nome: String, uid: String) {
    // Cor de fundo amarela, conforme a imagem de referência
    val backgroundColor = Color(0xFFE2C64D)

    // Usamos o layout reutilizável que criamos
    LobbyLayout(
        navController = navController,
        nome = nome,
        backgroundColor = backgroundColor,
        menuItems = listOf(
            Pair("Pacientes") { navController.navigate("${AppRoutes.PACIENTES_VINCULADOS}/$uid/${TipoUsuario.PROFISSIONAL_SAUDE}") },
            Pair("Vincular Paciente") { navController.navigate("${AppRoutes.VINCULAR_PACIENTE}/$uid/${TipoUsuario.PROFISSIONAL_SAUDE}") }
        )
    )
}