package com.tazy.simplepillfinal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tazy.simplepillfinal.AppRoutes
import com.tazy.simplepillfinal.AppRoutes.TelaProximasFisioterapiaPaciente
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
        composable(AppRoutes.TelaCadastroPacienteProf) { TelaCadastroPacienteProf(navController = navController) }
        // Bem-vindas

        composable(
            route = "${AppRoutes.BemVindoPaciente}/{nome}",
            arguments = listOf(
                navArgument("nome") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nome = backStackEntry.arguments?.getString("nome") ?: ""
            TelaBemVindoPaciente(navController = navController, nome = nome)
        }

        composable(
            route = "${AppRoutes.BemVindoCuidador}/{nome}",
            arguments = listOf(
                navArgument("nome") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nome = backStackEntry.arguments?.getString("nome") ?: ""
            TelaBemVindoCuidador(navController = navController, nome = nome)
        }

        composable(
            route = "${AppRoutes.BemVindoProfissional}/{nome}",
            arguments = listOf(
                navArgument("nome") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nome = backStackEntry.arguments?.getString("nome") ?: ""
            TelaBemVindoProfissionalSaude(navController = navController, nome = nome)
        }

        // Informaçoes de saúde Paciente

        composable(route = AppRoutes.TelaInformacoesSaudePaciente) {
            TelaInformacoesSaudePaciente(navController)
        }
        composable(AppRoutes.TelaConsultasPaciente) {
            TelaConsultasPaciente(navController)
        }
        composable(AppRoutes.TelaProximasConsultasPaciente) {
            TelaProximasConsultasPaciente(navController)
        }
        composable(AppRoutes.TelaConsultasAnterioresPaciente) {
            // TODO: Sua TelaConsultasAnteriores(navController)
        }

        composable(AppRoutes.TelaExamesPaciente) {
            TelaExamesPaciente(navController)
        }
        composable(AppRoutes.TelaProximosExamesPaciente) {
            TelaProximosExamesPaciente(navController)
        }
        composable(AppRoutes.TelaExamesAnterioresPaciente) {
            // TODO: Sua  TelaExamesAnterioresPaciente(navController)
        }

        composable(AppRoutes.TelaVacinasPaciente) {
            TelaVacinasPaciente(navController)
        }
        composable(AppRoutes.TelaProximasVacinasPaciente) {
            TelaProximasVacinasPaciente(navController)
        }
        composable(AppRoutes.TelaDosesAnterioresPaciente) {
            // TODO: Sua TelaDosesAnterioresPaciente(navController)
        }
        composable(AppRoutes.TelaRotinaDeExerciciosPaciente) {
            TelaRotinaDeExerciciosPaciente(navController)
        }


            composable(AppRoutes.TelaRotinaDeExerciciosPaciente) {
                TelaRotinaDeExerciciosPaciente(navController)
            }
        // No NavGraph.kt, adicione:
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
        composable(AppRoutes.TelaProximasConsultasNutricionista) {
            TelaProximasConsultasPaciente(navController)
        }
        composable(AppRoutes.TelaSaudeMentalPaciente) {
            TelaSaudeMentalPaciente(navController)
        }
        composable(AppRoutes.TelaProximasSaudeMentalPaciente) {
            TelaProximasSaudeMentalPaciente(navController)
        }
        composable(AppRoutes.TelaMedicacaoPaciente) {
            TelaMedicacaoPaciente(
                onVoltar = { navController.popBackStack() }
            )


        }
    }
}

        // (Faça o mesmo para as outras telas de Bem‑vindo, se tiver parâmetros)
