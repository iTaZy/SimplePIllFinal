// ARQUIVO: ui/screens/TelaPerfil.kt
package com.tazy.simplepillfinal.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tazy.simplepillfinal.R
import com.tazy.simplepillfinal.model.Cuidador
import com.tazy.simplepillfinal.model.Paciente
import com.tazy.simplepillfinal.model.ProfissionalSaude
import com.tazy.simplepillfinal.model.TipoUsuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaPerfil(
    navController: NavController,
    uid: String,
    tipo: TipoUsuario,
    viewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = uid, key2 = tipo) {
        viewModel.loadUser(uid, tipo)
    }

    LaunchedEffect(viewModel.saveSuccess) {
        if (viewModel.saveSuccess) {
            Toast.makeText(context, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
            viewModel.clearState()
        }
    }

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            Toast.makeText(context, "Erro: $it", Toast.LENGTH_LONG).show()
        }
    }

    val backgroundColor = when (tipo) {
        TipoUsuario.PACIENTE -> Color(0xFF74ABBF)
        TipoUsuario.CUIDADOR -> Color(0xFF166A1E)
        TipoUsuario.PROFISSIONAL_SAUDE -> Color(0xFFE2C64D)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F2F5))
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_person_placeholder),
                            contentDescription = "Foto de Perfil",
                            modifier = Modifier.size(120.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = viewModel.nome,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Tipo de usuário: ${tipo.name.replace("_", " ").lowercase().replaceFirstChar { it.titlecase() }}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text("Informações de Contato", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Divider()

                            OutlinedTextField(
                                value = viewModel.email,
                                onValueChange = { viewModel.email = it },
                                label = { Text("E-mail") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = viewModel.telefone,
                                onValueChange = { viewModel.telefone = it },
                                label = { Text("Número de Telefone") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = viewModel.endereco,
                                onValueChange = { viewModel.endereco = it },
                                label = { Text("Endereço Completo") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    when (viewModel.user) {
                        is Paciente -> {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Text("Dados de Paciente", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Divider()
                                    OutlinedTextField(
                                        value = viewModel.nationalId,
                                        onValueChange = { },
                                        label = { Text("CPF") },
                                        modifier = Modifier.fillMaxWidth(),
                                        readOnly = true
                                    )
                                    OutlinedTextField(
                                        value = viewModel.susCard,
                                        onValueChange = { viewModel.susCard = it },
                                        label = { Text("Carteirinha do SUS") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                        is Cuidador -> {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Text("Dados de Cuidador", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Divider()
                                    OutlinedTextField(
                                        value = viewModel.nationalId,
                                        onValueChange = { },
                                        label = { Text("CPF") },
                                        modifier = Modifier.fillMaxWidth(),
                                        readOnly = true
                                    )
                                }
                            }
                        }
                        is ProfissionalSaude -> {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Text("Dados de Profissional de Saúde", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Divider()
                                    OutlinedTextField(
                                        value = viewModel.nationalId,
                                        onValueChange = { },
                                        label = { Text("CRM") },
                                        modifier = Modifier.fillMaxWidth(),
                                        readOnly = true
                                    )
                                    OutlinedTextField(
                                        value = viewModel.ufCrm,
                                        onValueChange = { },
                                        label = { Text("UF do CRM") },
                                        modifier = Modifier.fillMaxWidth(),
                                        readOnly = true
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.onSaveClick(uid, tipo) },
                        enabled = !viewModel.isLoading,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(56.dp)
                    ) {
                        Text(if (viewModel.isLoading) "Salvando..." else "Salvar Alterações", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        if (viewModel.showPasswordDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.clearState() },
                title = { Text("Confirme sua senha") },
                text = {
                    Column {
                        Text("Para alterar seu e-mail, por favor, insira sua senha atual.")
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = viewModel.currentPassword,
                            onValueChange = { viewModel.currentPassword = it },
                            label = { Text("Senha atual") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.onConfirmPasswordAndSave(uid, tipo) },
                        enabled = viewModel.currentPassword.isNotBlank()
                    ) {
                        Text("Confirmar")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { viewModel.clearState() }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
