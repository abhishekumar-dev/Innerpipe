package com.example.innertube.model.renderers

import com.example.innertube.model.NavigationEndpoint
import com.example.innertube.model.common.Runs
import kotlinx.serialization.Serializable

@Serializable
data class ChipCloudChipRenderer(
    val text: Runs,
    val navigationEndpoint: NavigationEndpoint
)