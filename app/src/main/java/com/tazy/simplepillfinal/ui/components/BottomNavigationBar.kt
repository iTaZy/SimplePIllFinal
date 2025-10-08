// F_ARQUIVO: ui/components/BottomNavigationBar.kt
package com.tazy.simplepillfinal.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People // Importação corrigida
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.CheckCircle
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
    object Home : BottomNavItem(AppRoutes.TELA_INICIAL, "Início", Icons.Default.Home)
    object Pacientes : BottomNavItem(AppRoutes.PACIENTES_VINCULADOS, "Pacientes", Icons.Default.People)
    object Profissionais : BottomNavItem(AppRoutes.PROFISSIONAIS_VINCULADOS, "Profissionais", Icons.Default.Person)
    object Confirmacoes : BottomNavItem(AppRoutes.CONFIRMACOES_VINCULO, "Confirmações", Icons.Default.CheckCircle)
    object Sair : BottomNavItem(AppRoutes.TELA_INICIAL, "Sair", Icons.Default.ExitToApp)
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
            BottomNavItem.Sair
        )
        TipoUsuario.CUIDADOR, TipoUsuario.PROFISSIONAL_SAUDE -> listOf(
            BottomNavItem.Home,
            BottomNavItem.Pacientes,
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
                    if (item.label == "Sair") {
                        AuthRepository().signOut()
                        navController.navigate(AppRoutes.TELA_INICIAL) {
                            popUpTo(AppRoutes.TELA_INICIAL) { inclusive = true }
                        }
                    } else {
                        val route = when (item) {
                            is BottomNavItem.Pacientes -> "${AppRoutes.PACIENTES_VINCULADOS}/$uid/${tipo}"
                            is BottomNavItem.Profissionais -> "${AppRoutes.PROFISSIONAIS_VINCULADOS}/$uid"
                            is BottomNavItem.Confirmacoes -> "${AppRoutes.CONFIRMACOES_VINCULO}/$uid"
                            else -> item.route
                        }
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(text = item.label, fontSize = 12.sp) }
            )
        }
    }
}