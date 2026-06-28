package com.example.aichat.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Formatea un timestamp (Long, millis) a cadena "hh:mm a" localizada.
 */
fun Long.toTimeString(): String =
    SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(this))

/**
 * Formatea un timestamp a "dd/MM/yyyy HH:mm".
 */
fun Long.toDateTimeString(): String =
    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(this))

/**
 * Devuelve un string descriptivo relativo ("Hoy", "Ayer", fecha).
 */
fun Long.toRelativeDay(): String {
    val now = System.currentTimeMillis()
    val dayMs = 24 * 60 * 60 * 1000L
    val today = now / dayMs
    val thatDay = this / dayMs
    return when (today - thatDay) {
        0L -> "Hoy"
        1L -> "Ayer"
        else -> SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(this))
    }
}

/**
 * Devuelve las iniciales de un nombre (max 2 chars).
 */
fun String.initials(): String {
    val parts = trim().split(" ").filter { it.isNotEmpty() }
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> (parts[0].take(1) + parts[1].take(1)).uppercase()
    }
}
