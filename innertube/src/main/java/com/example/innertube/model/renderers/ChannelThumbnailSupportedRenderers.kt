package com.example.innertube.model.renderers

import kotlinx.serialization.Serializable

@Serializable
data class ChannelThumbnailSupportedRenderers(
    private val channelThumbnailWithLinkRenderer: ChannelThumbnailWithLinkRenderer
) {
    val url = channelThumbnailWithLinkRenderer.thumbnail.url
}