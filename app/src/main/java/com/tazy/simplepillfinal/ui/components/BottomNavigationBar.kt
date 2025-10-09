// F_ARQUIVO: ui/components/BottomNavigationBar.kt
package com.tazy.simplepillfinal.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.AccountCircle // NEW: Add this import
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tazy.simplepillfinal.data.AuthRepository
import com.tazy.simplepillfinal.model.TipoUsuario
import com.tazy.simplepillfinal.navigation.AppRoutes

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItem(AppRoutes.BEM_VINDO_PACIENTE, "Início", Icons.Default.Home)
    object Pacientes : BottomNavItem(AppRoutes.PACIENTES_VINCULADOS, "Pacientes", Icons.Default.People)
    object Profissionais : BottomNavItem(AppRoutes.PROFISSIONAIS_VINCULADOS, "Profissionais", Icons.Default.Person)
    object Confirmacoes : BottomNavItem(AppRoutes.CONFIRMACOES_VINCULO, "Confirmações", Icons.Default.CheckCircle)
    object Sair : BottomNavItem(AppRoutes.TELA_INICIAL, "Sair", Icons.AutoMirrored.Filled.ExitToApp)
    object Perfil : BottomNavItem(AppRoutes.TELA_PERFIL, "Perfil", Icons.Default.AccountCircle) // NEW
}

@Composable
fun BottomNavigationBar(navController: NavController, uid: String, tipo: TipoUsuario) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navItems = when (tipo) {
        TipoUsuario.PACIENTE -> listOf(
            BottomNavItem.Home,
            BottomNavItem.Profissionais,
            BottomNavItem.Confirmacoes,
            BottomNavItem.Perfil, // NEW
            BottomNavItem.Sair
        )
        TipoUsuario.CUIDADOR, TipoUsuario.PROFISSIONAL_SAUDE -> listOf(
            BottomNavItem.Home,
            BottomNavItem.Pacientes,
            BottomNavItem.Perfil, // NEW
            BottomNavItem.Sair
        )
    }

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        navItems.forEach { item ->
            val selected = currentRoute?.startsWith(item.route) ?: false

            NavigationBarItem(
                selected = selected,
                onClick = {
                    when (item) {
                        is BottomNavItem.Home -> {
                            val route = when(tipo) {
                                TipoUsuario.PACIENTE -> "${AppRoutes.BEM_VINDO_PACIENTE}/$uid"
                                TipoUsuario.CUIDADOR -> "${AppRoutes.BEM_VINDO_CUIDADOR}/$uid"
                                TipoUsuario.PROFISSIONAL_SAUDE -> "${AppRoutes.BEM_VINDO_PROFISSIONAL}/$uid"
                            }
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        is BottomNavItem.Pacientes -> {
                            navController.navigate("${AppRoutes.PACIENTES_VINCULADOS}/$uid/${tipo}") {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        is BottomNavItem.Profissionais -> {
                            navController.navigate("${AppRoutes.PROFISSIONAIS_VINCULADOS}/$uid") {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        is BottomNavItem.Confirmacoes -> {
                            navController.navigate("${AppRoutes.CONFIRMACOES_VINCULO}/$uid") {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        is BottomNavItem.Perfil -> { // NEW
                            navController.navigate("${AppRoutes.TELA_PERFIL}/$uid/$tipo") {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        is BottomNavItem.Sair -> {
                            AuthRepository().signOut()
                            navController.navigate(AppRoutes.TELA_INICIAL) {
                                popUpTo(AppRoutes.TELA_INICIAL) { inclusive = true }
                            }
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(text = item.label, fontSize = 12.sp) }
            )
        }
    }
}