// NOVO ARQUIVO: ui/screens/TelaDetalhesRegistro.kt
package com.tazy.simplepillfinal.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.tazy.simplepillfinal.model.*
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalhesRegistro(
    navController: NavController,
    tipoAcao: String,
    registroId: String,
    viewModel: DetalhesRegistroViewModel = viewModel()
) {
    val context = LocalContext.current
    val decodedTipoAcao = remember(tipoAcao) {
        URLDecoder.decode(tipoAcao, StandardCharsets.UTF_8.toString())
    }

    LaunchedEffect(key1 = tipoAcao, key2 = registroId) {
        viewModel.carregarRegistro(decodedTipoAcao, registroId)
    }

    val headerColor = Color(0xFF74ABBF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
    ) {
        // Onda superior
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .align(Alignment.TopCenter)
        ) {
            val w = size.width
            val h = size.height
            drawPath(
                path = Path().apply {
                    moveTo(0f, h * 0.6f)
                    cubicTo(w * 0.25f, h * 1.2f, w * 0.75f, h * 0.5f, w, h * 0.8f)
                    lineTo(w, 0f)
                    lineTo(0f, 0f)
                    close()
                },
                color = headerColor
            )
        }

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(decodedTipoAcao, color = Color.White) },
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
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                when {
                    viewModel.isLoading -> CircularProgressIndicator()
                    viewModel.errorMessage != null -> Text(text = "Erro: ${viewModel.errorMessage}")
                    viewModel.registro == null -> Text(text = "Registro não encontrado.")
                    else -> {
                        DetalhesRegistroCard(
                            registro = viewModel.registro!!,
                            onOpenPdf = { url ->
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse(url)
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Não foi possível abrir o arquivo.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetalhesRegistroCard(registro: Any, onOpenPdf: (String) -> Unit) {
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            when (registro) {
                is Medicacao -> MedicacaoDetails(medicacao = registro, onOpenPdf)
                is Exame -> ExameDetails(exame = registro, onOpenPdf)
                is Vacinacao -> VacinacaoDetails(vacinacao = registro, onOpenPdf)
                is Internacao -> InternacaoDetails(internacao = registro, onOpenPdf)
                is Fisioterapia -> FisioterapiaDetails(fisioterapia = registro, onOpenPdf)
                is SaudeMental -> SaudeMentalDetails(saudeMental = registro, onOpenPdf)
                is Nutricao -> NutricaoDetails(nutricao = registro, onOpenPdf)
            }
        }
    }
}

@Composable
fun MedicacaoDetails(medicacao: Medicacao, onOpenPdf: (String) -> Unit) {
    Text(text = "Medicação", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(16.dp))
    InfoItem("Nome:", medicacao.nome)
    InfoItem("Dosagem:", medicacao.dosagem)
    InfoItem("Frequência:", medicacao.frequencia)
    InfoItem("Duração:", medicacao.duracao)
    InfoItem("Observações:", medicacao.observacoes)
    InfoItem("Prescrito por:", medicacao.profissionalNome)
    InfoItem("Data de Prescrição:", formatTimestamp(medicacao.dataPrescricao))
    if (!medicacao.arquivoUrl.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onOpenPdf(medicacao.arquivoUrl) }, modifier = Modifier.fillMaxWidth()) {
            Text("Abrir Arquivo PDF")
        }
    }
}

@Composable
fun ExameDetails(exame: Exame, onOpenPdf: (String) -> Unit) {
    Text(text = "Exame", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(16.dp))
    InfoItem("Exame Solicitado:", exame.examePedido)
    InfoItem("Unidade:", exame.unidade)
    InfoItem("Diagnóstico:", exame.diagnostico)
    InfoItem("Data de Solicitação:", formatTimestamp(exame.dataSolicitacao))
    if (!exame.arquivoUrl.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onOpenPdf(exame.arquivoUrl) }, modifier = Modifier.fillMaxWidth()) {
            Text("Abrir Arquivo PDF")
        }
    }
}

@Composable
fun VacinacaoDetails(vacinacao: Vacinacao, onOpenPdf: (String) -> Unit) {
    Text(text = "Vacinação", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(16.dp))
    InfoItem("Vacina 1:", vacinacao.vacina1)
    InfoItem("Vacina 2:", vacinacao.vacina2)
    InfoItem("Vacina 3:", vacinacao.vacina3)
    InfoItem("Vacina 4:", vacinacao.vacina4)
    InfoItem("Data de Registro:", formatTimestamp(vacinacao.dataSolicitacao))
    if (!vacinacao.arquivoUrl.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onOpenPdf(vacinacao.arquivoUrl) }, modifier = Modifier.fillMaxWidth()) {
            Text("Abrir Arquivo PDF")
        }
    }
}

@Composable
fun InternacaoDetails(internacao: Internacao, onOpenPdf: (String) -> Unit) {
    Text(text = "Internação", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(16.dp))
    InfoItem("Unidade:", internacao.unidade)
    InfoItem("Motivo:", internacao.motivo)
    InfoItem("Data:", internacao.data)
    InfoItem("Data de Registro:", formatTimestamp(internacao.dataRegistro))
    if (!internacao.arquivoUrl.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onOpenPdf(internacao.arquivoUrl) }, modifier = Modifier.fillMaxWidth()) {
            Text("Abrir Arquivo PDF")
        }
    }
}

@Composable
fun FisioterapiaDetails(fisioterapia: Fisioterapia, onOpenPdf: (String) -> Unit) {
    Text(text = "Fisioterapia", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(16.dp))
    InfoItem("Data:", fisioterapia.data)
    InfoItem("Local:", fisioterapia.local)
    InfoItem("Sessões:", fisioterapia.sessoes)
    InfoItem("Diagnóstico:", fisioterapia.diagnostico)
    InfoItem("Data de Registro:", formatTimestamp(fisioterapia.dataRegistro))
    if (!fisioterapia.arquivoUrl.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onOpenPdf(fisioterapia.arquivoUrl) }, modifier = Modifier.fillMaxWidth()) {
            Text("Abrir Arquivo PDF")
        }
    }
}

@Composable
fun SaudeMentalDetails(saudeMental: SaudeMental, onOpenPdf: (String) -> Unit) {
    Text(text = "Saúde Mental", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(16.dp))
    InfoItem("Unidade:", saudeMental.unidade)
    InfoItem("Tratamento:", saudeMental.tratamento)
    InfoItem("Data:", saudeMental.data)
    InfoItem("Duração:", saudeMental.duracao)
    InfoItem("Data de Registro:", formatTimestamp(saudeMental.dataRegistro))
    if (!saudeMental.arquivoUrl.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onOpenPdf(saudeMental.arquivoUrl) }, modifier = Modifier.fillMaxWidth()) {
            Text("Abrir Arquivo PDF")
        }
    }
}

@Composable
fun NutricaoDetails(nutricao: Nutricao, onOpenPdf: (String) -> Unit) {
    Text(text = "Nutrição", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(16.dp))
    InfoItem("Data:", nutricao.data)
    InfoItem("Local:", nutricao.local)
    InfoItem("Diagnóstico:", nutricao.diagnostico)
    InfoItem("Data de Registro:", formatTimestamp(nutricao.dataRegistro))
    if (!nutricao.arquivoUrl.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onOpenPdf(nutricao.arquivoUrl) }, modifier = Modifier.fillMaxWidth()) {
            Text("Abrir Arquivo PDF")
        }
    }
}

@Composable
private fun InfoItem(label: String, value: String) {
    if (value.isNotBlank()) {
        Column(modifier = Modifier.padding(bottom = 8.dp)) {
            Text(
                text = label,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black.copy(alpha = 0.8f)
            )
            Text(
                text = value,
                color = Color.Black.copy(alpha = 0.6f)
            )
        }
    }
}