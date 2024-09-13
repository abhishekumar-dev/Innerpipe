package com.example.innertube.model.renderers

import kotlinx.serialization.Serializable

@Serializable
data class MetadataBadgeRenderer(
    val label: String?,
    val tooltip: String?
)