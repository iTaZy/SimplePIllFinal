// F_ARQUIVO: navigation/NavGraph.kt
package com.tazy.simplepillfinal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tazy.simplepillfinal.ui.screens.TelaCadastroPaciente
// Importe suas outras telas aqui

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppRoutes.TELA_INICIAL) {
        // composable(AppRoutes.TELA_INICIAL) { TelaInicial(navController) }
        composable(AppRoutes.CADASTRO_PACIENTE) { TelaCadastroPaciente(navController) }
        // Adicione outras telas aqui
    }
}