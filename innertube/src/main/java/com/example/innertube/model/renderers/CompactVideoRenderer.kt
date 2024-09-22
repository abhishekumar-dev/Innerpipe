package com.example.innertube.model.renderers

import kotlinx.serialization.Serializable

@Serializable
data class CompactVideoRenderer(
    val compactVideoRenderer: VideoRenderer?,
    val continuationItemRenderer: ContinuationItemRenderer?
)