// F_ARQUIVO: MainActivity.kt
package com.tazy.simplepillfinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.tazy.simplepillfinal.navigation.NavGraph
import com.tazy.simplepillfinal.ui.theme.SimplePillTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimplePillTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}