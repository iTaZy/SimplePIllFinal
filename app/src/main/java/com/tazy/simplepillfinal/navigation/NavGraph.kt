// F_ARQUIVO: navigation/NavGraph.kt
package com.tazy.simplepillfinal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tazy.simplepillfinal.CadastroScreen
import com.tazy.simplepillfinal.ui.screens.TelaCadastroGeral // Importe a nova tela

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppRoutes.TELA_INICIAL) {
        composable(AppRoutes.TELA_INICIAL) { CadastroScreen(navController) }
        composable(AppRoutes.CADASTRO_GERAL) { TelaCadastroGeral(navController) }
        // Adicione as rotas para as telas "Bem-vindo" aqui
    }
}