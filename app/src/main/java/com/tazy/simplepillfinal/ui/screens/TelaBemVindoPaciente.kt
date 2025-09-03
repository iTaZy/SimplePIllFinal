// CRIE ESTE NOVO ARQUIVO: ui/screens/TelaBemVindoPaciente.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tazy.simplepillfinal.navigation.AppRoutes

@Composable
fun TelaBemVindoPaciente(navController: NavController, nome: String, email: String, uid: String) {
    val backgroundColor = Color(0xFF74ABBF) // Azul claro da sua imagem

    LobbyLayout(
        navController = navController,
        nome = nome,
        backgroundColor = backgroundColor
    ) {
        // Botões específicos do Paciente
        // ATUALIZAÇÃO AQUI
        LobbyButton(text = "Minhas Medicações") {
            navController.navigate("${AppRoutes.MINHAS_MEDICACOES}/$uid")
        }
        Spacer(Modifier.height(24.dp))
        LobbyButton(text = "Cadastrar itens") {
            // TODO: Implementar navegação para a tela de cadastrar itens
        }
    }
}