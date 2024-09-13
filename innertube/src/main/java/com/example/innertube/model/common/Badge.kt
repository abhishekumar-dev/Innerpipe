package com.example.innertube.model.common

import com.example.innertube.model.renderers.MetadataBadgeRenderer
import kotlinx.serialization.Serializable

@Serializable
data class Badge(
    val metadataBadgeRenderer: MetadataBadgeRenderer
)