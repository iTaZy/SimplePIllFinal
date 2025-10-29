// F_ARQUIVO: navigation/NavGraph.kt
package com.tazy.simplepillfinal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tazy.simplepillfinal.CadastroScreen
import com.tazy.simplepillfinal.model.TipoUsuario
import com.tazy.simplepillfinal.ui.screens.*
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AppRoutes.TELA_INICIAL) {

        composable(AppRoutes.TELA_INICIAL) { CadastroScreen(navController) }
        composable(AppRoutes.CADASTRO_GERAL) { TelaCadastroGeral(navController) }

        composable(
            route = "${AppRoutes.BEM_VINDO_PACIENTE}/{uid}",
            arguments = listOf(
                navArgument("uid") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val uid = backStackEntry.arguments?.getString("uid") ?: ""
            TelaBemVindoPaciente(navController = navController, nome = "", email = "", uid = uid)
        }

        composable(
            route = "${AppRoutes.BEM_VINDO_CUIDADOR}/{uid}",
            arguments = listOf(
                navArgument("uid") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val uid = backStackEntry.arguments?.getString("uid") ?: ""
            TelaBemVindoCuidador(navController = navController, nome = "", uid = uid)
        }

        composable(
            route = "${AppRoutes.BEM_VINDO_PROFISSIONAL}/{uid}",
            arguments = listOf(
                navArgument("uid") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val uid = backStackEntry.arguments?.getString("uid") ?: ""
            TelaBemVindoProfissionalSaude(navController = navController, nome = "", uid = uid)
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

        // --- ROTA ATUALIZADA (MELHORIA 4) ---
        composable(
            route = "${AppRoutes.ACOES_PACIENTE}/{pacienteUid}/{pacienteNome}",
            arguments = listOf(
                navArgument("pacienteUid") { type = NavType.StringType },
                navArgument("pacienteNome") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val pacienteUid = backStackEntry.arguments?.getString("pacienteUid") ?: ""
            val pacienteNome = backStackEntry.arguments?.getString("pacienteNome") ?: "Paciente"
            val decodedPacienteNome = URLDecoder.decode(pacienteNome, StandardCharsets.UTF_8.toString())

            // Lista de ações para Profissionais
            val professionalActions = listOf(
                Pair("Prescrever medicações") {
                    val encodedNome = URLEncoder.encode(decodedPacienteNome, StandardCharsets.UTF_8.toString())
                    navController.navigate("${AppRoutes.PRESCREVER_MEDICACAO}/$pacienteUid/$encodedNome")
                },
                Pair("Solicitar exames") {
                    val encodedNome = URLEncoder.encode(decodedPacienteNome, StandardCharsets.UTF_8.toString())
                    val encodedAcao = URLEncoder.encode("Exames", StandardCharsets.UTF_8.toString())
                    navController.navigate("${AppRoutes.CADASTRO_UNIFICADO}/$pacienteUid/$encodedNome/$encodedAcao")
                },
                Pair("Solicitar vacinação") {
                    val encodedNome = URLEncoder.encode(decodedPacienteNome, StandardCharsets.UTF_8.toString())
                    val encodedAcao = URLEncoder.encode("Vacinação", StandardCharsets.UTF_8.toString())
                    navController.navigate("${AppRoutes.CADASTRO_UNIFICADO}/$pacienteUid/$encodedNome/$encodedAcao")
                },
                Pair("Registrar internação") {
                    val encodedNome = URLEncoder.encode(decodedPacienteNome, StandardCharsets.UTF_8.toString())
                    val encodedAcao = URLEncoder.encode("Internação", StandardCharsets.UTF_8.toString())
                    navController.navigate("${AppRoutes.CADASTRO_UNIFICADO}/$pacienteUid/$encodedNome/$encodedAcao")
                },
                Pair("Cadastro fisioterapêutico") {
                    val encodedNome = URLEncoder.encode(decodedPacienteNome, StandardCharsets.UTF_8.toString())
                    val encodedAcao = URLEncoder.encode("Fisioterapia", StandardCharsets.UTF_8.toString())
                    navController.navigate("${AppRoutes.CADASTRO_UNIFICADO}/$pacienteUid/$encodedNome/$encodedAcao")
                },
                Pair("Cadastro saúde mental") {
                    val encodedNome = URLEncoder.encode(decodedPacienteNome, StandardCharsets.UTF_8.toString())
                    val encodedAcao = URLEncoder.encode("Saúde mental", StandardCharsets.UTF_8.toString())
                    navController.navigate("${AppRoutes.CADASTRO_UNIFICADO}/$pacienteUid/$encodedNome/$encodedAcao")
                },
                Pair("Cadastro nutricional") {
                    val encodedNome = URLEncoder.encode(decodedPacienteNome, StandardCharsets.UTF_8.toString())
                    val encodedAcao = URLEncoder.encode("Nutrição", StandardCharsets.UTF_8.toString())
                    navController.navigate("${AppRoutes.CADASTRO_UNIFICADO}/$pacienteUid/$encodedNome/$encodedAcao")
                }
            )

            TelaPacienteAcoes(
                navController = navController,
                pacienteNome = pacienteNome,
                backgroundColor = Color(0xFFE2C64D), // Amarelo
                contentColor = Color.Black,
                menuItems = professionalActions
            )
        }

        // --- ROTA ATUALIZADA (MELHORIA 4) ---
        composable(
            route = "${AppRoutes.VISUALIZAR_DADOS_PACIENTE}/{pacienteUid}/{pacienteNome}",
            arguments = listOf(
                navArgument("pacienteUid") { type = NavType.StringType },
                navArgument("pacienteNome") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val pacienteUid = backStackEntry.arguments?.getString("pacienteUid") ?: ""
            val pacienteNome = backStackEntry.arguments?.getString("pacienteNome") ?: "Paciente"

            // Lista de ações para Cuidadores
            val caregiverActions = listOf(
                Pair("Visualizar Medicações") {
                    navController.navigate("${AppRoutes.MINHAS_MEDICACOES}/$pacienteUid")
                },
                Pair("Visualizar Exames") {
                    navController.navigate("${AppRoutes.VISUALIZAR_EXAMES}/$pacienteUid")
                },
                Pair("Visualizar Vacinação") {
                    navController.navigate("${AppRoutes.VISUALIZAR_VACINACAO}/$pacienteUid")
                },
                Pair("Visualizar Internações") {
                    navController.navigate("${AppRoutes.VISUALIZAR_INTERNACOES}/$pacienteUid")
                },
                Pair("Visualizar Fisioterapia") {
                    navController.navigate("${AppRoutes.VISUALIZAR_FISIOTERAPIA}/$pacienteUid")
                },
                Pair("Visualizar Saúde Mental") {
                    navController.navigate("${AppRoutes.VISUALIZAR_SAUDE_MENTAL}/$pacienteUid")
                },
                Pair("Visualizar Nutrição") {
                    navController.navigate("${AppRoutes.VISUALIZAR_NUTRICAO}/$pacienteUid")
                }
            )

            TelaPacienteAcoes(
                navController = navController,
                pacienteNome = pacienteNome,
                backgroundColor = Color(0xFF166A1E), // Verde
                contentColor = Color.White,
                menuItems = caregiverActions
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

        // NOVA ROTA PARA DETALHES DO REGISTRO
        composable(
            route = "${AppRoutes.DETALHES_REGISTRO}/{tipoAcao}/{registroId}",
            arguments = listOf(
                navArgument("tipoAcao") { type = NavType.StringType },
                navArgument("registroId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val tipoAcao = backStackEntry.arguments?.getString("tipoAcao") ?: ""
            val registroId = backStackEntry.arguments?.getString("registroId") ?: ""
            TelaDetalhesRegistro(navController = navController, tipoAcao = tipoAcao, registroId = registroId)
        }

        // NOVO: Rota para a tela de perfil
        composable(
            route = "${AppRoutes.TELA_PERFIL}/{uid}/{tipo}",
            arguments = listOf(
                navArgument("uid") { type = NavType.StringType },
                navArgument("tipo") { type = NavType.EnumType(TipoUsuario::class.java) }
            )
        ) { backStackEntry ->
            val uid = backStackEntry.arguments?.getString("uid") ?: ""
            val tipo = backStackEntry.arguments?.getSerializable("tipo") as TipoUsuario
            TelaPerfil(navController = navController, uid = uid, tipo = tipo)
        }
    }
}