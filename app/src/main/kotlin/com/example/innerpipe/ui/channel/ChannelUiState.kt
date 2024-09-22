package com.example.innerpipe.ui.channel

import com.example.innertube.model.Filter
import com.example.innertube.model.renderers.VideoRenderer

data class Header(
    val title: String,
    val description: String,
    val avatar: String,
    val banner: String,
    val handle: String,
    val subscribers: String,
    val videosCount: String
)

data class ChannelUiState(
    val header: Header? = null,
    val videos: List<VideoRenderer> = emptyList(),
    val filters: List<Filter> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)