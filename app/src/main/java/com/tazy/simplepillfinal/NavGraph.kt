package com.tazy.simplepillfinal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tazy.simplepillfinal.AppRoutes
import com.tazy.simplepillfinal.CadastroScreen
import com.tazy.simplepillfinal.ui.screens.TelaBemVindoCuidador
import com.tazy.simplepillfinal.ui.screens.TelaBemVindoPaciente
import com.tazy.simplepillfinal.ui.screens.TelaBemVindoProfissionalSaude
import com.tazy.simplepillfinal.ui.screens.TelaCadastroCuidador
import com.tazy.simplepillfinal.ui.screens.TelaCadastroPaciente
import com.tazy.simplepillfinal.ui.screens.TelaCadastroPacienteProf
import com.tazy.simplepillfinal.ui.screens.TelaCadastroProfissionalDaSaude
import com.tazy.simplepillfinal.ui.screens.TelaConsultasPaciente
import com.tazy.simplepillfinal.ui.screens.TelaExamesPaciente
import com.tazy.simplepillfinal.ui.screens.TelaExerciciosDiaPaciente
import com.tazy.simplepillfinal.ui.screens.TelaInformacoesSaudePaciente
import com.tazy.simplepillfinal.ui.screens.TelaProximasConsultasPaciente
import com.tazy.simplepillfinal.ui.screens.TelaProximasVacinasPaciente
import com.tazy.simplepillfinal.ui.screens.TelaProximosExamesPaciente
import com.tazy.simplepillfinal.ui.screens.TelaRotinaDeExerciciosPaciente
import com.tazy.simplepillfinal.ui.screens.TelaVacinasPaciente
import com.tazy.simplepillfinal.ui.screens.TelaFisioterapiaPaciente
import com.tazy.simplepillfinal.ui.screens.TelaProximasFisioterapiaPaciente
import com.tazy.simplepillfinal.ui.screens.TelaConsultasNutricionista
import com.tazy.simplepillfinal.ui.screens.TelaMedicacaoPaciente
import com.tazy.simplepillfinal.ui.screens.TelaProximasSaudeMentalPaciente
import com.tazy.simplepillfinal.ui.screens.TelaSaudeMentalPaciente


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = AppRoutes.TelaInicial) {
        // Tela inicial de escolha/login
        composable(AppRoutes.TelaInicial) { CadastroScreen(navController) }

        // Cadastro
        composable(AppRoutes.CadastroPaciente) { TelaCadastroPaciente(navController) }
        composable(AppRoutes.CadastroCuidador) { TelaCadastroCuidador(navController) }
        composable(AppRoutes.CadastroProfissional) { TelaCadastroProfissionalDaSaude(navController) }
        composable(AppRoutes.TelaCadastroPacienteProf) { TelaCadastroPacienteProf(navController) }

        // Bem-vindas
        composable(
            route = "${AppRoutes.BemVindoPaciente}/{nome}/{email}",
            arguments = listOf(
                navArgument("nome") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nome = backStackEntry.arguments?.getString("nome") ?: ""
            val email = backStackEntry.arguments?.getString("email") ?: ""
            TelaBemVindoPaciente(navController = navController, nome = nome, email = email)
        }


        composable(
            route = "${AppRoutes.BemVindoCuidador}/{nome}",
            arguments = listOf(navArgument("nome") { type = NavType.StringType })
        ) { backStackEntry ->
            val nome = backStackEntry.arguments?.getString("nome") ?: ""
            TelaBemVindoCuidador(navController = navController, nome = nome)
        }

        composable(
            route = "${AppRoutes.BemVindoProfissional}/{nome}",
            arguments = listOf(navArgument("nome") { type = NavType.StringType })
        ) { backStackEntry ->
            val nome = backStackEntry.arguments?.getString("nome") ?: ""
            TelaBemVindoProfissionalSaude(navController = navController, nome = nome)
        }

        // Informações de saúde do paciente (agora recebe email!)
        composable(
            route = "${AppRoutes.TelaInformacoesSaudePaciente}/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            TelaInformacoesSaudePaciente(navController, pacienteEmail = email)
        }

        // Outras telas (sem mudanças)
        composable(AppRoutes.TelaConsultasPaciente) {
            TelaConsultasPaciente(navController)
        }
        composable(AppRoutes.TelaProximasConsultasPaciente) {
            TelaProximasConsultasPaciente(navController)
        }
        composable(AppRoutes.TelaExamesPaciente) {
            TelaExamesPaciente(navController)
        }
        composable(AppRoutes.TelaProximosExamesPaciente) {
            TelaProximosExamesPaciente(navController)
        }
        composable(AppRoutes.TelaVacinasPaciente) {
            TelaVacinasPaciente(navController)
        }
        composable(AppRoutes.TelaProximasVacinasPaciente) {
            TelaProximasVacinasPaciente(navController)
        }
        composable(AppRoutes.TelaRotinaDeExerciciosPaciente) {
            TelaRotinaDeExerciciosPaciente(navController)
        }
        composable(
            route = "${AppRoutes.TelaExerciciosDiaPaciente}/{dia}",
            arguments = listOf(navArgument("dia") { type = NavType.StringType })
        ) { backStackEntry ->
            val dia = backStackEntry.arguments?.getString("dia") ?: ""
            TelaExerciciosDiaPaciente(navController, dia)
        }
        composable(AppRoutes.TelaFisioterapiaPaciente) {
            TelaFisioterapiaPaciente(navController)
        }
        composable(AppRoutes.TelaProximasFisioterapiaPaciente) {
            TelaProximasFisioterapiaPaciente(navController)
        }
        composable(AppRoutes.TelaConsultasNutricionista) {
            TelaConsultasNutricionista(navController)
        }
        composable(AppRoutes.TelaSaudeMentalPaciente) {
            TelaSaudeMentalPaciente(navController)
        }
        composable(AppRoutes.TelaProximasSaudeMentalPaciente) {
            TelaProximasSaudeMentalPaciente(navController)
        }
        composable(AppRoutes.TelaMedicacaoPaciente) {
            TelaMedicacaoPaciente(onVoltar = { navController.popBackStack() })
        }
    }
}
