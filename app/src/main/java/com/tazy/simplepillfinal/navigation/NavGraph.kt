// F_ARQUIVO: navigation/NavGraph.kt
package com.tazy.simplepillfinal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tazy.simplepillfinal.CadastroScreen
import com.tazy.simplepillfinal.model.TipoUsuario
import com.tazy.simplepillfinal.ui.screens.*
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AppRoutes.SPLASH_SCREEN) {

        // Tela de Splash
        composable(AppRoutes.SPLASH_SCREEN) { SplashScreen(navController) }

        composable(AppRoutes.TELA_INICIAL) { CadastroScreen(navController) }
        composable(AppRoutes.CADASTRO_GERAL) { TelaCadastroGeral(navController) }

        composable(
            route = "${AppRoutes.BEM_VINDO_PACIENTE}/{nome}/{email}/{uid}",
            arguments = listOf(
                navArgument("nome") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType },
                navArgument("uid") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nome = backStackEntry.arguments?.getString("nome") ?: "Usuário"
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val uid = backStackEntry.arguments?.getString("uid") ?: ""
            TelaBemVindoPaciente(navController = navController, nome = nome, email = email, uid = uid)
        }

        composable(
            route = "${AppRoutes.BEM_VINDO_CUIDADOR}/{nome}/{email}/{uid}",
            arguments = listOf(
                navArgument("nome") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType },
                navArgument("uid") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nome = backStackEntry.arguments?.getString("nome") ?: "Usuário"
            val uid = backStackEntry.arguments?.getString("uid") ?: ""
            TelaBemVindoCuidador(navController = navController, nome = nome, uid = uid)
        }

        composable(
            route = "${AppRoutes.BEM_VINDO_PROFISSIONAL}/{nome}/{email}/{uid}",
            arguments = listOf(
                navArgument("nome") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType },
                navArgument("uid") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nome = backStackEntry.arguments?.getString("nome") ?: "Usuário"
            val uid = backStackEntry.arguments?.getString("uid") ?: ""
            TelaBemVindoProfissionalSaude(navController = navController, nome = nome, uid = uid)
        }

        composable(
            route = "${AppRoutes.VINCULAR_PACIENTE}/{associadoUid}/{associadoTipo}",
            arguments = listOf(
                navArgument("associadoUid") { type = NavType.StringType },
                navArgument("associadoTipo") { type = NavType.EnumType(TipoUsuario::class.java) }
            )
        ) { backStackEntry ->
            val associadoUid = backStackEntry.arguments?.getString("associadoUid") ?: ""
            val associadoTipo = backStackEntry.arguments?.getSerializable("associadoTipo") as TipoUsuario
            TelaVincularPaciente(
                navController = navController,
                associadoUid = associadoUid,
                associadoTipo = associadoTipo
            )
        }

        composable(
            route = "${AppRoutes.PACIENTES_VINCULADOS}/{uid}/{tipo}",
            arguments = listOf(
                navArgument("uid") { type = NavType.StringType },
                navArgument("tipo") { type = NavType.EnumType(TipoUsuario::class.java) }
            )
        ) { backStackEntry ->
            val uid = backStackEntry.arguments?.getString("uid") ?: ""
            val tipo = backStackEntry.arguments?.getSerializable("tipo") as TipoUsuario
            TelaPacientesVinculados(navController, uid, tipo)
        }

        composable(
            route = "${AppRoutes.DETALHES_MEDICO}/{pacienteUid}/{medicoId}/{medicoNome}",
            arguments = listOf(
                navArgument("pacienteUid") { type = NavType.StringType },
                navArgument("medicoId") { type = NavType.StringType },
                navArgument("medicoNome") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val pacienteUid = backStackEntry.arguments?.getString("pacienteUid") ?: ""
            val medicoId = backStackEntry.arguments?.getString("medicoId") ?: ""
            val medicoNome = URLDecoder.decode(backStackEntry.arguments?.getString("medicoNome") ?: "", StandardCharsets.UTF_8.toString())
            TelaDetalhesMedico(navController = navController, pacienteUid = pacienteUid, medicoId = medicoId, medicoNome = medicoNome)
        }

        composable(
            route = "${AppRoutes.PROFISSIONAIS_VINCULADOS}/{pacienteUid}",
            arguments = listOf(navArgument("pacienteUid") { type = NavType.StringType })
        ) { backStackEntry ->
            val pacienteUid = backStackEntry.arguments?.getString("pacienteUid") ?: ""
            TelaProfissionaisVinculados(navController, pacienteUid)
        }

        composable(
            route = "${AppRoutes.CONFIRMACOES_VINCULO}/{pacienteUid}",
            arguments = listOf(navArgument("pacienteUid") { type = NavType.StringType })
        ) { backStackEntry ->
            val pacienteUid = backStackEntry.arguments?.getString("pacienteUid") ?: ""
            TelaConfirmacoes(navController, pacienteUid)
        }

        composable(
            route = "${AppRoutes.ACOES_PACIENTE}/{pacienteUid}/{pacienteNome}",
            arguments = listOf(
                navArgument("pacienteUid") { type = NavType.StringType },
                navArgument("pacienteNome") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val pacienteUid = backStackEntry.arguments?.getString("pacienteUid") ?: ""
            val pacienteNome = backStackEntry.arguments?.getString("pacienteNome") ?: "Paciente"
            TelaAcoesPaciente(
                navController = navController,
                pacienteUid = pacienteUid,
                pacienteNome = pacienteNome
            )
        }

        composable(
            route = "${AppRoutes.VISUALIZAR_DADOS_PACIENTE}/{pacienteUid}/{pacienteNome}",
            arguments = listOf(
                navArgument("pacienteUid") { type = NavType.StringType },
                navArgument("pacienteNome") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val pacienteUid = backStackEntry.arguments?.getString("pacienteUid") ?: ""
            val pacienteNome = backStackEntry.arguments?.getString("pacienteNome") ?: "Paciente"
            TelaVisualizacaoPaciente(
                navController = navController,
                pacienteUid = pacienteUid,
                pacienteNome = pacienteNome
            )
        }

        composable(
            route = "${AppRoutes.PRESCREVER_MEDICACAO}/{pacienteUid}/{pacienteNome}",
            arguments = listOf(
                navArgument("pacienteUid") { type = NavType.StringType },
                navArgument("pacienteNome") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val pacienteUid = backStackEntry.arguments?.getString("pacienteUid") ?: ""
            val pacienteNomeEncoded = backStackEntry.arguments?.getString("pacienteNome") ?: "Paciente"
            val pacienteNome = URLDecoder.decode(pacienteNomeEncoded, StandardCharsets.UTF_8.toString())
            TelaPrescreverMedicacao(navController, pacienteUid, pacienteNome)
        }

        composable(
            route = "${AppRoutes.MINHAS_MEDICACOES}/{pacienteUid}",
            arguments = listOf(
                navArgument("pacienteUid") { type = NavType.StringType }
            )
        ) {backStackEntry ->
            val pacienteUid = backStackEntry.arguments?.getString("pacienteUid") ?: ""
            TelaMinhasMedicacoes(navController, pacienteUid)
        }

        composable(
            route = "${AppRoutes.CADASTRO_UNIFICADO}/{pacienteUid}/{pacienteNome}/{acao}",
            arguments = listOf(
                navArgument("pacienteUid") { type = NavType.StringType },
                navArgument("pacienteNome") { type = NavType.StringType },
                navArgument("acao") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val pacienteUid = backStackEntry.arguments?.getString("pacienteUid") ?: ""
            val pacienteNome = backStackEntry.arguments?.getString("pacienteNome") ?: "Paciente"
            val acao = backStackEntry.arguments?.getString("acao") ?: ""
            TelaCadastroUnificado(navController, pacienteUid, pacienteNome, acao)
        }

        composable(
            route = "${AppRoutes.VISUALIZAR_EXAMES}/{pacienteUid}",
            arguments = listOf(navArgument("pacienteUid") { type = NavType.StringType })
        ) { backStackEntry ->
            val pacienteUid = backStackEntry.arguments?.getString("pacienteUid") ?: ""
            VisualizacaoGeralScreen(navController, pacienteUid, "Exames")
        }

        composable(
            route = "${AppRoutes.VISUALIZAR_VACINACAO}/{pacienteUid}",
            arguments = listOf(navArgument("pacienteUid") { type = NavType.StringType })
        ) { backStackEntry ->
            val pacienteUid = backStackEntry.arguments?.getString("pacienteUid") ?: ""
            VisualizacaoGeralScreen(navController, pacienteUid, "Vacinação")
        }

        composable(
            route = "${AppRoutes.VISUALIZAR_INTERNACOES}/{pacienteUid}",
            arguments = listOf(navArgument("pacienteUid") { type = NavType.StringType })
        ) { backStackEntry ->
            val pacienteUid = backStackEntry.arguments?.getString("pacienteUid") ?: ""
            VisualizacaoGeralScreen(navController, pacienteUid, "Internações")
        }

        composable(
            route = "${AppRoutes.VISUALIZAR_FISIOTERAPIA}/{pacienteUid}",
            arguments = listOf(navArgument("pacienteUid") { type = NavType.StringType })
        ) { backStackEntry ->
            val pacienteUid = backStackEntry.arguments?.getString("pacienteUid") ?: ""
            VisualizacaoGeralScreen(navController, pacienteUid, "Fisioterapia")
        }

        composable(
            route = "${AppRoutes.VISUALIZAR_SAUDE_MENTAL}/{pacienteUid}",
            arguments = listOf(navArgument("pacienteUid") { type = NavType.StringType })
        ) { backStackEntry ->
            val pacienteUid = backStackEntry.arguments?.getString("pacienteUid") ?: ""
            VisualizacaoGeralScreen(navController, pacienteUid, "Saúde Mental")
        }

        composable(
            route = "${AppRoutes.VISUALIZAR_NUTRICAO}/{pacienteUid}",
            arguments = listOf(navArgument("pacienteUid") { type = NavType.StringType })
        ) { backStackEntry ->
            val pacienteUid = backStackEntry.arguments?.getString("pacienteUid") ?: ""
            VisualizacaoGeralScreen(navController, pacienteUid, "Nutrição")
        }
    }
}