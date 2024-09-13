package com.example.innertube.model.renderers

import com.example.innertube.model.common.Thumbnail
import kotlinx.serialization.Serializable

@Serializable
data class ChannelThumbnailWithLinkRenderer(
    val thumbnail: Thumbnail
)