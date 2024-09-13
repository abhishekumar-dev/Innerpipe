package com.example.innertube.model.renderers

import com.example.innertube.model.common.Content
import com.example.innertube.model.common.Runs
import kotlinx.serialization.Serializable

@Serializable
data class ShelfRenderer(
    val title: Runs,
    val content: Content
)