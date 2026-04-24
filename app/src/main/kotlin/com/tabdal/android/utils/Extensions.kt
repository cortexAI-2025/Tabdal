package com.tabdal.android.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toFormattedPrice(): String {
    val formatter = NumberFormat.getNumberInstance(Locale.FRANCE)
    return "${formatter.format(this)} MAD"
}

fun Long.toFormattedDate(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
    return sdf.format(Date(this))
}

fun Long.toRelativeTime(): String {
    val now = System.currentTimeMillis()
    val diff = now - this
    return when {
        diff < 60_000 -> "À l'instant"
        diff < 3_600_000 -> "${diff / 60_000}min"
        diff < 86_400_000 -> "${diff / 3_600_000}h"
        diff < 604_800_000 -> "${diff / 86_400_000}j"
        else -> toFormattedDate()
    }
}

fun String.toInitials(): String {
    return split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercaseChar() }.joinToString("")
}

fun Double.toFormattedSurface(): String = "${String.format("%.0f", this)} m²"
