package com.example.innertube.model.common

import com.example.innertube.model.NavigationEndpoint
import kotlinx.serialization.Serializable

@Serializable
data class Runs(
    private val runs: List<Run>?,
    private val simpleText: String?
) {
    @Serializable
    data class Run(
        val text: String,
        val navigationEndpoint: NavigationEndpoint?
    )
    val text = runs?.joinToString("") { it.text } ?: simpleText.orEmpty()
    val first = runs?.firstOrNull()
    val browseId = first?.navigationEndpoint?.browseEndpoint?.browseId
}