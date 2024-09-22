package com.example.innerpipe

import android.content.Context
import android.content.Intent
import java.util.Locale
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

fun Long.toHumanReadableCount(): String {
    val array = arrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
    val value = floor(log10(this.toDouble())).toInt()
    val base = value / 3
    return if (value >= 3 && base < array.size) {
        String.format(
            Locale.getDefault(),
            "%.1f",
            this / 10.0.pow((base * 3).toDouble())
        ) + array[base]
    } else {
        this.toString().reversed().chunked(3).joinToString(",").reversed()
    }
}

fun Context.share(id: String) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=$id")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}