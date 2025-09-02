// F_ARQUIVO: ui/screens/TelaBemVindoCuidador.kt
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
fun TelaBemVindoCuidador(navController: NavController, nome: String, uid: String) {
    // Cor de fundo verde, conforme a imagem de referência
    val backgroundColor = Color(0xFF166A1E)

    // Usamos o layout reutilizável que criamos
    LobbyLayout(
        navController = navController,
        nome = nome,
        backgroundColor = backgroundColor
    ) {
        // Botões específicos do Lobby do Cuidador
        LobbyButton(text = "Informações de saúde") {
            navController.navigate("${AppRoutes.PACIENTES_VINCULADOS}/$uid/${TipoUsuario.CUIDADOR}")
        }
        Spacer(Modifier.height(24.dp))
        LobbyButton(text = "Vincular Paciente") {
            // Navega para a tela de vínculo, passando o UID e o Tipo de Usuário do Cuidador
            navController.navigate("${AppRoutes.VINCULAR_PACIENTE}/$uid/${TipoUsuario.CUIDADOR}")
        }
    }
}