package com.example.innertube.model.renderers

import com.example.innertube.model.common.Content
import kotlinx.serialization.Serializable

@Serializable
data class SectionListRenderer(
    val contents: List<Content>
)