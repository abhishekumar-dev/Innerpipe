package com.example.innertube.model

import com.example.innertube.model.renderers.FeedFilterChipBarRenderer
import kotlinx.serialization.Serializable

@Serializable
data class HeaderForFeedFilter(
    val feedFilterChipBarRenderer: FeedFilterChipBarRenderer
)