package com.example.innertube.model.renderers

import com.example.innertube.model.common.Content
import kotlinx.serialization.Serializable

@Serializable
data class TabRenderer(
    val title: String,
    val content: Content?,
    val selected: Boolean?
)