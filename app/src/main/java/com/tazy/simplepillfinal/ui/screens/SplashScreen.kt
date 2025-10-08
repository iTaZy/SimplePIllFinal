// F_ARQUIVO: ui/screens/SplashScreen.kt
package com.tazy.simplepillfinal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tazy.simplepillfinal.R
import com.tazy.simplepillfinal.navigation.AppRoutes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_simple_pill),
            contentDescription = "Logo Simple Pill",
            modifier = Modifier.size(200.dp)
        )
    }

    LaunchedEffect(key1 = true) {
        delay(2000L) // Exibe a splash screen por 2 segundos
        navController.navigate(AppRoutes.TELA_INICIAL) {
            popUpTo(AppRoutes.SPLASH_SCREEN) { inclusive = true }
        }
    }
}