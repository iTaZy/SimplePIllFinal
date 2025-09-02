// F_ARQUIVO: navigation/NavGraph.kt
package com.tazy.simplepillfinal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController // Importe este se estiver faltando
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tazy.simplepillfinal.CadastroScreen
import com.tazy.simplepillfinal.model.TipoUsuario
import com.tazy.simplepillfinal.ui.screens.*

@Composable
// CORREÇÃO: Adicione o parâmetro aqui
fun NavGraph(navController: NavHostController) {
    // A linha "val navController = rememberNavController()" é removida daqui,
    // pois ele agora é recebido como parâmetro.

    NavHost(navController = navController, startDestination = AppRoutes.TELA_INICIAL) {

        composable(AppRoutes.TELA_INICIAL) { CadastroScreen(navController) }

        composable(AppRoutes.CADASTRO_GERAL) { TelaCadastroGeral(navController) }

        // ROTA PARA O LOBBY DO PACIENTE
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

        // ROTA PARA O LOBBY DO CUIDADOR
        composable(
            route = "${AppRoutes.BEM_VINDO_CUIDADOR}/{nome}/{email}/{uid}",
            arguments = listOf(
                navArgument("nome") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType },
                navArgument("uid") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nome = backStackEntry.arguments?.getString("nome") ?: "Usuário"
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val uid = backStackEntry.arguments?.getString("uid") ?: ""
            TelaBemVindoCuidador(navController = navController, nome = nome, uid = uid)
        }

        // ROTA PARA O LOBBY DO PROFISSIONAL DA SAÚDE
        composable(
            route = "${AppRoutes.BEM_VINDO_PROFISSIONAL}/{nome}/{email}/{uid}",
            arguments = listOf(
                navArgument("nome") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType },
                navArgument("uid") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nome = backStackEntry.arguments?.getString("nome") ?: "Usuário"
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val uid = backStackEntry.arguments?.getString("uid") ?: ""
            TelaBemVindoProfissionalSaude(navController = navController, nome = nome, uid = uid)
        }

        // ROTA PARA VINCULAR PACIENTE
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

        // ROTA PARA PACIENTES VINCULADOS
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
    }
}