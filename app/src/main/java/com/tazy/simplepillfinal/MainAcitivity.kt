// MainActivity.kt
// F_ARQUIVO: MainActivity.kt
package com.tazy.simplepillfinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tazy.simplepillfinal.navigation.NavGraph
import com.tazy.simplepillfinal.ui.theme.SimplePillTheme // O nome do seu tema

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimplePillTheme { // Use o seu tema aqui
                NavGraph()
            }
        }
    }
}