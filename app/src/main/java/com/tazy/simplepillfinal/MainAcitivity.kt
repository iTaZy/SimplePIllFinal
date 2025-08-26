// F_ARQUIVO: MainActivity.kt
package com.tazy.simplepillfinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController // <-- IMPORTANTE: Adicione esta linha
import com.tazy.simplepillfinal.navigation.NavGraph
import com.tazy.simplepillfinal.ui.theme.SimplePillTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimplePillTheme {
                // CORREÇÃO: Crie o NavController aqui
                val navController = rememberNavController()
                // E passe-o para o NavGraph
                NavGraph(navController = navController)
            }
        }
    }
}