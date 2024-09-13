package com.example.innertube

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("unused")
private fun extractID(url: String): String? {
    val regex = Regex("""^.*(youtu\.be/|vi?/|u/\w/|embed/|\?vi?=|&vi?=)([^#&?]*).*""")
    val match = regex.find(url) ?: return null
    return match.groupValues[2]
}

fun Long.unixTimestampToDateTime(): String {
    val date = Date(this * 1000)
    return SimpleDateFormat("M/d/yy, h:mm a", Locale.getDefault()).format(date)
}

fun String.toListOfSuggestions(): List<String> {
    return Json.parseToJsonElement(substringAfter("(").substringBeforeLast(")")).jsonArray[1].jsonArray.map {
        it.jsonArray.first().jsonPrimitive.content
    }
}