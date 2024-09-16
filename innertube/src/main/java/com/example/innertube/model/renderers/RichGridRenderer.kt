package com.example.innertube.model.renderers

import com.example.innertube.model.HeaderForFeedFilter
import com.example.innertube.model.common.Content
import kotlinx.serialization.Serializable

@Serializable
data class RichGridRenderer(
    val contents: List<Content>,
    val header: HeaderForFeedFilter
)