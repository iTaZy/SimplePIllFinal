package com.tazy.simplepillfinal.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.tazy.simplepillfinal.R
import com.tazy.simplepillfinal.ui.components.BotaoOval
import com.tazy.simplepillfinal.AppRoutes
import kotlinx.coroutines.tasks.await

@Composable
fun TelaInformacoesSaudePaciente(navController: NavController, pacienteEmail: String) {
    val azulFundo = Color(0xFF7AA7B4)
    val context = LocalContext.current

    var nome by remember { mutableStateOf("") }
    var idade by remember { mutableStateOf("") }

    // Buscar informações do paciente ao abrir a tela
    LaunchedEffect(pacienteEmail) {
        val db = FirebaseFirestore.getInstance()
        try {
            val snapshot = db.collection("pacientes")
                .whereEqualTo("email", pacienteEmail)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                val paciente = snapshot.documents.first()
                nome = paciente.getString("nome") ?: "Nome não encontrado"
                idade = paciente.getString("idade") ?: "Idade não informada"
            } else {
                Toast.makeText(context, "Paciente não encontrado", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Erro ao buscar dados: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(azulFundo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_person_placeholder),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = if (nome.isNotEmpty() && idade.isNotEmpty()) "$nome, $idade anos" else "Carregando...",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(Modifier.height(24.dp))

        // Botões
        BotaoOval("Consultas") {
            navController.navigate(AppRoutes.TelaConsultasPaciente)
        }
        BotaoOval("Exames") {
            navController.navigate(AppRoutes.TelaExamesPaciente)
        }
        BotaoOval("Rotina de Exercícios") {
            navController.navigate(AppRoutes.TelaRotinaDeExerciciosPaciente)
        }
        BotaoOval("Fisioterapia") {
            navController.navigate(AppRoutes.TelaFisioterapiaPaciente)
        }
        BotaoOval("Internação") {
            // navController.navigate(AppRoutes.TelaInternacao)
        }
        BotaoOval("Medicações") {
            navController.navigate(AppRoutes.TelaMedicacaoPaciente)
        }
        BotaoOval("Nutrição") {
            navController.navigate(AppRoutes.TelaConsultasNutricionista)
        }
        BotaoOval("Saúde Mental") {
            navController.navigate(AppRoutes.TelaSaudeMentalPaciente)
        }
        BotaoOval("Vacinas") {
            navController.navigate(AppRoutes.TelaVacinasPaciente)
        }

        Spacer(Modifier.height(15.dp))

        TextButton(onClick = { navController.popBackStack() }) {
            Text(
                "Voltar",
                color = Color.Black,
                fontSize = 16.sp,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}
