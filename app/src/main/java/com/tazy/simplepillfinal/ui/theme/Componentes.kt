package com.tazy.simplepillfinal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Botão oval branco, reutilizável em toda a UI
 */
@Composable
fun BotaoOval(
    texto: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = texto,
            fontSize = 18.sp,
            color = Color.Black
        )
    }
}


@Composable
fun InputCard(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    background: Color = Color(0xFFE0DDDC),
    dividerColor: Color = Color(0xFF7AA7B4),
    trailing: (@Composable (() -> Unit))? = null
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = background,
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = label, color = Color.Gray, fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(fontSize = 20.sp, color = Color.Black),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = if (trailing != null) 8.dp else 0.dp)
                )
                trailing?.invoke()
            }
            Spacer(Modifier.height(4.dp))
            Divider(color = dividerColor, thickness = 2.dp)
        }
    }
}
@Composable
fun CampoTextoPersonalizado(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(vertical = 2.dp)
    )
}
@Composable
fun CaixaInfo(
    titulo: String,
    info: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = titulo,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF7AA7B4)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = info,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}
