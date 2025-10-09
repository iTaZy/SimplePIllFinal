// NOVO ARQUIVO: ui/screens/Util.kt
package com.tazy.simplepillfinal.ui.screens

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun formatTimestamp(timestamp: Timestamp): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", Locale.getDefault())
    return sdf.format(timestamp.toDate())
}