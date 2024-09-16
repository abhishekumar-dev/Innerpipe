package com.example.innertube.model.renderers

import com.example.innertube.model.Tab
import kotlinx.serialization.Serializable

@Serializable
data class TwoColumnBrowseResultsRenderer(
    val tabs: List<Tab>
)