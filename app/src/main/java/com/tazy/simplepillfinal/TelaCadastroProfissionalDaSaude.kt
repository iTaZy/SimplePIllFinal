package com.tazy.simplepillfinal.ui.screens

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.tazy.simplepillfinal.R

@Composable
fun TelaCadastroProfissionalDaSaude(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Cabeçalho amarelo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color(0xFFE2C64D)), // Amarelo mostarda
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "Cadastro do Profissional da Saúde",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 50.dp)
            )
        }

        // Conteúdo branco com cantos arredondados
        Surface(
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 110.dp),
            color = Color.White
        ) {
            FormularioProfissional(navController)
        }
    }
}

@Composable
private fun FormularioProfissional(navController: NavController) {
    val context = LocalContext.current

    var nome by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var profissao by remember { mutableStateOf("") }
    var idade by remember { mutableStateOf("") }
    var endereco by remember { mutableStateOf("") }
    var emailPaciente by remember { mutableStateOf("") }
    var senhaPaciente by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        Image(
            painter = painterResource(id = R.drawable.ic_person_placeholder),
            contentDescription = "Avatar Profissional da Saúde",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        )

        Spacer(Modifier.height(24.dp))

        // Campos de texto
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = telefone,
            onValueChange = { telefone = it },
            label = { Text("Telefone") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = profissao,
            onValueChange = { profissao = it },
            label = { Text("Profissão") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = idade,
            onValueChange = { idade = it },
            label = { Text("Idade") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = endereco,
            onValueChange = { endereco = it },
            label = { Text("Endereço") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = emailPaciente,
            onValueChange = { emailPaciente = it },
            label = { Text("Email do Paciente") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = senhaPaciente,
            onValueChange = { senhaPaciente = it },
            label = { Text("Senha do Paciente") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        Spacer(Modifier.height(20.dp))

        // Botões
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Voltar",
                fontSize = 20.sp,
                textDecoration = TextDecoration.Underline,
                color = Color.Black,
                modifier = Modifier.clickable { navController.popBackStack() }
            )

            Button(
                onClick = {
                    if (nome.isBlank() || telefone.isBlank() || profissao.isBlank() || emailPaciente.isBlank() || senhaPaciente.isBlank()) {
                        Toast.makeText(context, "Preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT).show()
                    } else if (!emailPaciente.contains("@") || !emailPaciente.contains(".")) {
                        Toast.makeText(context, "Digite um email válido!", Toast.LENGTH_SHORT).show()
                    } else if (senhaPaciente.length < 6) {
                        Toast.makeText(context, "A senha deve ter no mínimo 6 caracteres!", Toast.LENGTH_SHORT).show()
                    } else {
                        val db = FirebaseFirestore.getInstance()
                        val profissional = hashMapOf(
                            "nome" to nome,
                            "telefone" to telefone,
                            "profissao" to profissao,
                            "idade" to idade,
                            "endereco" to endereco,
                            "emailPaciente" to emailPaciente,
                            "senhaPaciente" to senhaPaciente
                        )

                        db.collection("profissionaisDaSaude")
                            .add(profissional)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Cadastro salvo com sucesso!", Toast.LENGTH_SHORT).show()
                                Handler(Looper.getMainLooper()).postDelayed({
                                    navController.popBackStack()
                                }, 1500)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Erro ao salvar: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2C64D)),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = "Salvar",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}
