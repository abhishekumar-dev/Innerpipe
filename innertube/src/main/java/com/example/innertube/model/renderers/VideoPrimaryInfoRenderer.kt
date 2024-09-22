package com.example.innertube.model.renderers

import com.example.innertube.model.common.Runs
import kotlinx.serialization.Serializable

@Serializable
data class VideoPrimaryInfoRenderer(
    val viewCount: ViewCount,
    val dateText: Runs,
    val relativeDateText: Runs?,
    val title: Runs
) {
    @Serializable
    data class ViewCount(
        val videoViewCountRenderer: VideoViewCountRenderer,
    )
}