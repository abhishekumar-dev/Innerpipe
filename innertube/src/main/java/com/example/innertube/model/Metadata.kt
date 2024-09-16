package com.example.innertube.model

import com.example.innertube.model.renderers.ChannelMetadataRenderer
import kotlinx.serialization.Serializable

@Serializable
data class Metadata(
    val channelMetadataRenderer: ChannelMetadataRenderer
)