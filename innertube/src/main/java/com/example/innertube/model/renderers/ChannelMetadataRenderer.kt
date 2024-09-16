package com.example.innertube.model.renderers

import com.example.innertube.model.common.Thumbnail
import kotlinx.serialization.Serializable

@Serializable
data class ChannelMetadataRenderer(
    val title: String,
    val description: String,
    val rssUrl: String,
    val externalId: String,
    val avatar: Thumbnail,
    val channelUrl: String
)