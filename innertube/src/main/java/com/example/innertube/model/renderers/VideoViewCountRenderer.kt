package com.example.innertube.model.renderers

import com.example.innertube.model.common.Runs
import kotlinx.serialization.Serializable

@Serializable
data class VideoViewCountRenderer(
    val viewCount: Runs,
    val shortViewCount: Runs?
)