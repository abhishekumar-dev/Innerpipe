package com.example.innertube.model.renderers

import com.example.innertube.model.common.Badge
import com.example.innertube.model.common.Runs
import com.example.innertube.model.common.Thumbnail
import kotlinx.serialization.Serializable

@Serializable
data class VideoOwnerRenderer(
    val thumbnail: Thumbnail,
    val title: Runs,
    val subscriberCountText: Runs,
    val badges: List<Badge>?
)